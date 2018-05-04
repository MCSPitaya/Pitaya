package ch.pitaya.pitaya.service;

import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.AuthorizeCase;
import ch.pitaya.pitaya.exception.NotImplementedException;
import ch.pitaya.pitaya.model.Case;

@Service
public class CaseService {

	@AuthorizeCase(AuthCode.CASE_MODIFY)
	public void patchCase(Case caze) {
		// TODO: implement
		throw new NotImplementedException("case patch not yet implemented");
	}

}
