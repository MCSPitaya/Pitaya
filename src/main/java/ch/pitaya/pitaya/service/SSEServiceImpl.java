package ch.pitaya.pitaya.service;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
class SSEServiceImpl implements SSEService {

	private static final Logger logger = LoggerFactory.getLogger(SSEServiceImpl.class);

	private Map<Long, Map<String, Collection<SseEmitter>>> map = new HashMap<>();

	@Override
	public SseEmitter create(Long firmId, String endpoint) {
		// load the firm map
		Map<String, Collection<SseEmitter>> firmMap = map.get(firmId);
		if (firmMap == null) {
			synchronized (map) {
				if (!map.containsKey(firmId)) {
					logger.info("initializing firm map : " + firmId);
					firmMap = new HashMap<>();
					map.put(firmId, firmMap);
				} else {
					firmMap = map.get(firmId);
				}
			}
		}

		// load the endpoint list
		Collection<SseEmitter> list = firmMap.get(endpoint);
		if (list == null) {
			synchronized (firmMap) {
				if (!firmMap.containsKey(endpoint)) {
					logger.info("initializing endpoint list : " + firmId + "#" + endpoint);
					list = new ConcurrentLinkedQueue<>();
					firmMap.put(endpoint, list);
				} else {
					list = firmMap.get(endpoint);
				}
			}
		}
		// create Emitter
		logger.info("creating a new emitter : " + firmId + "#" + endpoint);
		SseEmitter emitter = new SseEmitter();
		emitter.onCompletion(() -> removeEmitter(firmId, endpoint, emitter));
		list.add(emitter);
		return emitter;
	}

	private void removeEmitter(Long firmId, String endpoint, SseEmitter emitter) {
		Map<String, Collection<SseEmitter>> firmMap = map.get(firmId);
		if (firmMap == null)
			return;
		Collection<SseEmitter> list = firmMap.get(endpoint);
		if (list == null)
			return;
		logger.info("removing emitter : " + firmId + "#" + endpoint);
		list.remove(emitter);
	}

	@Override
	public void emit(Long firmId, String endpoint, String type, Object payload) {
		Map<String, Collection<SseEmitter>> firmMap = map.get(firmId);
		if (firmMap == null)
			return;
		Collection<SseEmitter> list = firmMap.get(endpoint);
		if (list == null)
			return;
		for (SseEmitter emitter : list) {
			try {
				logger.info("sending data to : " + firmId + "#" + endpoint);
				emitter.send(SseEmitter.event().name(type).data(payload));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
