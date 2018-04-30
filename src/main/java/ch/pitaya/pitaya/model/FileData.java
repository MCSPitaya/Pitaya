package ch.pitaya.pitaya.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class FileData {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private File file;

	protected FileData() {
		// JPA
	}

	public FileData(File file) {
		this.file = file;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
