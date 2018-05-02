package ch.pitaya.pitaya.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notifications")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false)
	private Timestamp cre_dat;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, updatable = false)
	private User user;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "firm_id", nullable = false, updatable = false)
	private Firm firm;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "case_id", nullable = true, updatable = false)
	private Case theCase;

	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "file_id", nullable = true, updatable = false)
	private File file;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, updatable = false)
	private NotificationType type;

	private String notes;

	protected Notification() {
		// JPA
	}

	public Notification(NotificationType type, User user, Firm firm, Case theCase, File file, Timestamp time,
			String notes) {
		this.user = user;
		this.theCase = theCase;
		this.file = file;
		this.type = type;
		this.cre_dat = time;
		this.notes = notes;
		this.firm = firm;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Case getCase() {
		return theCase;
	}

	public File getFile() {
		return file;
	}

	public NotificationType getType() {
		return type;
	}

	public Timestamp getCreationTime() {
		return cre_dat;
	}

	public String getNotes() {
		return notes;
	}

	public Firm getFirm() {
		return firm;
	}

}
