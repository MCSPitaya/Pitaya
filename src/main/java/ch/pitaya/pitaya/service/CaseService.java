package ch.pitaya.pitaya.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.authorization.AuthCode;
import ch.pitaya.pitaya.authorization.Authorization;
import ch.pitaya.pitaya.exception.NotImplementedException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.payload.response.FileSummary;

@Service
public class CaseService {

	@Autowired
	private Authorization auth;

	public void patchCase(Case caze) {
		// TODO: implement
		throw new NotImplementedException("case patch not yet implemented");
	}

}
