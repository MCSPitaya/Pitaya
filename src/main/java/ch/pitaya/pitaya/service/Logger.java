package ch.pitaya.pitaya.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Logger {

	public org.slf4j.Logger get() {
		String className = Thread.currentThread().getStackTrace()[2].getClassName();
		return LoggerFactory.getLogger(className);
	}

}
