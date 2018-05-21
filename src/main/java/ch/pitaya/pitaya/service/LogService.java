package ch.pitaya.pitaya.service;

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
	public void logRequest(String ip, String endpoint, boolean hasToken, Long userId, String agent) {
		logger.get().info("logging request from " + ip + " to " + endpoint);
		requestLogRepo.save(new RequestLogEntry(ip, endpoint, hasToken, userId, agent));
	}

}
