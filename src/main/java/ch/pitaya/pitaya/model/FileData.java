package ch.pitaya.pitaya.model;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class FileData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private File file;
	
	@Lob
	@NotNull
	private Blob data;

	protected FileData() {
		// JPA
	}

	public FileData(File file, Blob blob) {
		this.file = file;
		this.data = blob;
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
	
	public Blob getData() {
		return this.data;
	}
	
	public void setData(Blob data) {
		this.data = data;
	}

}
