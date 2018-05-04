package ch.pitaya.pitaya.payload.response;

import ch.pitaya.pitaya.model.File;

public class FileSummary {

	private Long id;
	private String title;
	
	public FileSummary(File file) {
		id = file.getId();
		title = file.getName();
	}

	public FileSummary(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

}
