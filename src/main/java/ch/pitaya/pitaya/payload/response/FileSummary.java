package ch.pitaya.pitaya.payload.response;

public class FileSummary {

	private Long id;
	private String title;

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
