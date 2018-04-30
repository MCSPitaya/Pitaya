package ch.pitaya.pitaya.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Firm;
import ch.pitaya.pitaya.repository.CaseRepository;

@Service
public class CaseService {

	@Autowired
	private CaseRepository caseRepository;

	public List<Case> getCaseList(Firm firm) {
		return caseRepository.findByFirm(firm);
	}

}
