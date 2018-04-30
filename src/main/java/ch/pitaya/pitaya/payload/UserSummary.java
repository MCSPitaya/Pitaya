package ch.pitaya.pitaya.payload;

import ch.pitaya.pitaya.model.User;

public class UserSummary {
	private Long id;
	private String username;
	private String name;
	private String email;
	private String firm;

	public UserSummary(Long id, String username, String name, String email, String firm) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.email = email;
		this.firm = firm;
	}

	public UserSummary(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.name = user.getName();
		this.email = user.getEmail();
		this.firm = user.getFirm().getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirm() {
		return firm;
	}

	public void setFirm(String firm) {
		this.firm = firm;
	}
}
