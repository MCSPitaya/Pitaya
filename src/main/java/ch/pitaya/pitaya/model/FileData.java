package ch.pitaya.pitaya.model;

import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Column;
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
	
	@Column(nullable = false, updatable = false)
	private Timestamp cre_dat;
	
	@Lob
	@NotNull
	private Blob data;

	protected FileData() {
		// JPA
	}

	public FileData(File file, Blob blob) {
		this.file = file;
		this.data = blob;
		this.cre_dat = new Timestamp(System.currentTimeMillis());
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
	
	public int compare(FileData fd1, FileData fd2) {
		return Long.compare(fd1.getId(), fd2.getId());
	}
	
	public Timestamp getCreationTime() {
		return cre_dat;
	}

}
