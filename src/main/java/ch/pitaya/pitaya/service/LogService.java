package ch.pitaya.pitaya.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ch.pitaya.pitaya.model.RequestLogEntry;
import ch.pitaya.pitaya.repository.RequestLogEntryRepository;

@Service
public class LogService {

	@Autowired
	private RequestLogEntryRepository requestLogRepo;
	@Autowired
	private Logger logger;

	@Async
	public void logRequest(HttpServletRequest request, boolean hasToken, Long userId) {
		logger.get().info("logging request from " + request.getRemoteAddr() + " to " + request.getServletPath());
		requestLogRepo.save(new RequestLogEntry(request, hasToken, userId));
	}

}
