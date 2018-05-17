package ch.pitaya.pitaya.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SSEService {

	SseEmitter create(Long firmId, String path);

	void emit(Long firmId, String path, String type, Object payload);

}
