package ch.pitaya.pitaya.controller;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.pitaya.pitaya.exception.ResourceNotFoundException;
import ch.pitaya.pitaya.payload.response.CourtSummary;
import ch.pitaya.pitaya.repository.CourtRepository;

@RestController
@RequestMapping("/api/court")
public class CourtController {

	@Autowired
	private CourtRepository courtRepository;

	@GetMapping
	public Stream<CourtSummary> getCourtList() {
		return courtRepository.findAll().stream().map(CourtSummary::new);
	}

	@GetMapping("/{id}")
	public CourtSummary getCourt(@PathVariable("id") long id) {
		return courtRepository.findById(id).map(CourtSummary::new)
				.orElseThrow(() -> new ResourceNotFoundException("court", "id", id));
	}

}
