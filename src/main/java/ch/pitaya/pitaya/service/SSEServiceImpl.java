package ch.pitaya.pitaya.service;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
class SSEServiceImpl implements SSEService {

	private static class Endpoint {
		AtomicInteger clients = new AtomicInteger(0);
		ConcurrentLinkedQueue<SseEmitter> list = new ConcurrentLinkedQueue<>();
	}

	private static final Logger logger = LoggerFactory.getLogger(SSEServiceImpl.class);

	private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
	private static final ConcurrentHashMap<String, Endpoint> map = new ConcurrentHashMap<>();
	private static final AtomicInteger removalCount = new AtomicInteger(0);

	private final int EVICTION_THRESHOLD;
	
	public SSEServiceImpl(@Value("${pitaya.sse.eviction.threshold}") int threshold) {
		this.EVICTION_THRESHOLD = threshold;
	}

	@Override
	public void emit(Long firmId, String path, String type, Object payload) {
		Endpoint endpoint = map.get(firmId + "/" + path);
		if (endpoint == null)
			return;

		for (SseEmitter emitter : endpoint.list) {
			try {
				logger.info("sending data to : " + firmId + "/" + path);
				emitter.send(SseEmitter.event().name(type).data(payload));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public SseEmitter create(Long firmId, String path) {
		// setup
		LOCK.readLock().lock();
		Endpoint endpoint = map.computeIfAbsent(firmId + "/" + path, s -> new Endpoint());
		endpoint.clients.incrementAndGet();
		LOCK.readLock().unlock();

		// register
		logger.info("creating a new emitter : " + firmId + "/" + endpoint);
		SseEmitter emitter = new SseEmitter();
		emitter.onCompletion(() -> removeEmitter(firmId, path, emitter));
		endpoint.list.add(emitter);
		return emitter;

	}

	private void removeEmitter(Long firmId, String path, SseEmitter emitter) {
		// no need to lock, removal should always be safe
		Endpoint endpoint = map.get(firmId + "/" + path);
		if (endpoint == null)
			return;

		logger.info("removing emitter : " + firmId + "/" + path);
		endpoint.list.remove(emitter);
		endpoint.clients.decrementAndGet();
		removalCount.incrementAndGet();
	}

	@Scheduled(fixedRateString = "${pitaya.sse.eviction.rate}")
	public void evictEndpoints() {
		int removed = removalCount.get();
		if (removed < EVICTION_THRESHOLD) {
			logger.info("eviction threshold unmet: " + removed + " < " + EVICTION_THRESHOLD);
			return;
		}

		// do eviction
		LOCK.writeLock().lock();
		for (Entry<String, Endpoint> entry : map.entrySet()) {
			if (entry.getValue().clients.get() == 0) {
				logger.info("evicting endpoint: " + entry.getKey());
				map.remove(entry.getKey());
			}
		}
		LOCK.writeLock().unlock();
		removalCount.updateAndGet(i -> i - removed);
		logger.info("eviction completed");
	}

}
