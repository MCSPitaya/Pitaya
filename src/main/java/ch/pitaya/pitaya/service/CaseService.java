package ch.pitaya.pitaya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Court;
import ch.pitaya.pitaya.model.NotificationType;
import ch.pitaya.pitaya.model.User;
import ch.pitaya.pitaya.payload.request.PatchCaseRequest;
import ch.pitaya.pitaya.repository.CaseRepository;
import ch.pitaya.pitaya.repository.CourtRepository;

@Service
public class CaseService {

	@Autowired
	private CourtRepository courtRepo;

	@Autowired
	private CaseRepository caseRepo;

	@Autowired
	private NotificationService notifications;

	public boolean patchCase(Case caze, User user, PatchCaseRequest request) {
		boolean changed = false;

		if (request.title != null) {
			caze.setTitle(request.title);
			changed = true;
		}

		if (request.number != null) {
			caze.setCaseNumber(request.number);
			changed = true;
		}

		if (request.description != null) {
			caze.setDescription(request.description);
			changed = true;
		}

		if (request.courtId != null) {
			Court court = courtRepo.findById(request.courtId)
					.orElseThrow(() -> new ResourceNotFoundException("court", "id", request.courtId));
			caze.setCourt(court);
			changed = true;
		}

		if (changed) {
			caze.updateModification(user);
			caseRepo.save(caze);
			notifications.add(NotificationType.CASE_MODIFIED, user, caze);
		}

		return changed;

	}

}
