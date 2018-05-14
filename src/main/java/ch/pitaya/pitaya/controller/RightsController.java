package ch.pitaya.pitaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.authorization.Authorize;
import ch.pitaya.pitaya.authorization.AuthorizeCase;
import ch.pitaya.pitaya.authorization.AuthorizeFile;
import ch.pitaya.pitaya.exception.BadRequestException;
import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.AuthCodeChangeRequest;
import ch.pitaya.pitaya.payload.response.AuthCodeResponse;
import ch.pitaya.pitaya.payload.response.SimpleResponse;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.FileRepository;
import ch.pitaya.pitaya.repository.UserRepository;
import ch.pitaya.pitaya.security.SecurityFacade;
import ch.pitaya.pitaya.service.RightsService;

@RestController
@RequestMapping("/api")
public class RightsController {

	@Autowired
	private Authorization auth;

	@Autowired
	private SecurityFacade securityFacade;

	@Autowired
	private RightsService rightsService;

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private CaseRepository caseRepo;
	@Autowired
	private FileRepository fileRepo;

	// #### YOUR OWN RIHTS ####

	@GetMapping("/user/rights")
	public AuthCodeResponse getMyAuthCodes() {
		User user = securityFacade.getCurrentUser();
		return rightsService.getAuthCodes(user);
	}

	@GetMapping("/user/rights/case/{id}")
	public AuthCodeResponse getMyCaseCodes(@PathVariable Long id) {
		User user = securityFacade.getCurrentUser();
		return caseRepo.findByIdAndFirm(id, user.getFirm()) //
				.filter(c -> auth.test(c, AuthCode.CASE_READ)) //
				.map(c -> rightsService.getAuthCodes(user, c))
				.orElseThrow(() -> new ResourceNotFoundException("case", "id", id));
	}

	@GetMapping("/user/rights/file/{id}")
	public AuthCodeResponse getMyFileCodes(@PathVariable Long id) {
		User user = securityFacade.getCurrentUser();
		return fileRepo.findByIdAndTheCaseFirm(id, user.getFirm()) //
				.filter(f -> auth.test(f, AuthCode.FILE_READ)) //
				.map(f -> rightsService.getAuthCodes(user, f))
				.orElseThrow(() -> new ResourceNotFoundException("file", "id", id));
	}

	// #### ANOTHER GUY'S RIGTS

	@GetMapping("/user/{id}/rights")
	@Authorize(AuthCode.FIRM_READ_ROLES)
	public AuthCodeResponse getUserAuthCodes(@PathVariable Long id) {
		Firm firm = securityFacade.getCurrentFirm();
		return userRepo.findByIdAndFirm(id, firm) //
				.map(rightsService::getAuthCodes) //
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
	}

	@GetMapping("/user/{uid}/rights/case/{cid}")
	@AuthorizeCase(value = AuthCode.CASE_READ_ROLES, param = "cid")
	public AuthCodeResponse getCaseAuthCodes(@PathVariable("uid") Long uid, @PathVariable("cid") Long cid) {
		Firm firm = securityFacade.getCurrentFirm();
		Case c = caseRepo.findById(cid).get();
		return userRepo.findByIdAndFirm(uid, firm).map(u -> rightsService.getAuthCodes(u, c))
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", uid));
	}

	@GetMapping("/user/{uid}/rights/file/{fid}")
	@AuthorizeFile(value = AuthCode.FILE_READ_ROLES, param = "fid")
	public AuthCodeResponse getFileAuthCodes(@PathVariable("uid") Long uid, @PathVariable("fid") Long fid) {
		Firm firm = securityFacade.getCurrentFirm();
		File f = fileRepo.findByIdAndTheCaseFirm(fid, firm)
				.orElseThrow(() -> new ResourceNotFoundException("file", "id", fid));
		return userRepo.findByIdAndFirm(uid, firm).map(u -> rightsService.getAuthCodes(u, f))
				.orElseThrow(() -> new ResourceNotFoundException("user", "id", uid));
	}

	// #### SET SOME DUDES RIGHTS ####

	@PatchMapping("/user/{uid}/rights")
	@Authorize(AuthCode.FIRM_CHANGE_ROLES)
	public ResponseEntity<?> setUserAuthCodes(@PathVariable long uid, AuthCodeChangeRequest r) {
		User user = securityFacade.getCurrentUser();
		if (uid == user.getId())
			throw new BadRequestException("cannot modify your own rights!");
		return userRepo.findByIdAndFirm(uid, user.getFirm()) //
				.map(u -> {
					rightsService.setAuthCodesSafe(r, u);
					return SimpleResponse.ok("update successful");
				}).orElseThrow(() -> new ResourceNotFoundException("user", "id", uid));
	}

	@PatchMapping("/user/{uid}/rights/case/{cid}")
	@AuthorizeCase(value = AuthCode.CASE_CHANGE_ROLES, param = "cid")
	public ResponseEntity<?> getCaseAuthCodes(@PathVariable("uid") long uid, @PathVariable("cid") Long cid,
			AuthCodeChangeRequest r) {
		User user = securityFacade.getCurrentUser();
		if (uid == user.getId())
			throw new BadRequestException("cannot modify your own rights!");
		Firm firm = user.getFirm();
		Case c = caseRepo.findByIdAndFirm(cid, firm)
				.orElseThrow(() -> new ResourceNotFoundException("case", "id", cid));
		return userRepo.findByIdAndFirm(uid, firm).map(u -> {
			rightsService.setAuthCodes(r, u, c);
			return SimpleResponse.ok("update successful");
		}).orElseThrow(() -> new ResourceNotFoundException("user", "id", uid));
	}

	@PatchMapping("/user/{uid}/rights/file/{fid}")
	@AuthorizeFile(value = AuthCode.FILE_CHANGE_ROLES, param = "fid")
	public ResponseEntity<?> getFileAuthCodes(@PathVariable("uid") long uid, @PathVariable("fid") Long fid,
			AuthCodeChangeRequest r) {
		User user = securityFacade.getCurrentUser();
		if (uid == user.getId())
			throw new BadRequestException("cannot modify your own rights!");
		Firm firm = user.getFirm();
		File f = fileRepo.findByIdAndTheCaseFirm(fid, firm)
				.orElseThrow(() -> new ResourceNotFoundException("file", "id", fid));
		return userRepo.findByIdAndFirm(uid, firm).map(u -> {
			rightsService.setAuthCodes(r, u, f);
			return SimpleResponse.ok("update successful");
		}).orElseThrow(() -> new ResourceNotFoundException("user", "id", uid));
	}

}
