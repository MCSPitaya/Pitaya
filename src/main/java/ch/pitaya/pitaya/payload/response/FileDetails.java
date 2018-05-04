package ch.pitaya.pitaya.payload.response;

import ch.pitaya.pitaya.model.File;

public class FileDetails {
	
	private Long id;
	private String name;
	
	public FileDetails(File f) {
		this.id = f.getId();
		this.name = f.getName();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
