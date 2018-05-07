package ch.pitaya.pitaya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.cache.CachedInterceptor;

@RestController
@RequestMapping("/tech")
public class TechController {

	@Autowired
	private CachedInterceptor cachedInterceptor;

	@GetMapping("/cache")
	public String getCacheInfo() {
		return cachedInterceptor.getCacheStats().toString();
	}

}
