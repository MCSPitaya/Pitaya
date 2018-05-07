package ch.pitaya.pitaya.payload.response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.Client;

public class CaseDetails {

	private Long id;
	private String number;
	private String title;
	private String description;
	private Timestamp created, modified;
	private UserSummary createdBy, modifiedBy;
	private CourtSummary court;
	private List<ClientDetails> clients = new ArrayList<>();

	public CaseDetails(Case c) {
		this.id = c.getId();
		this.number = c.getCaseNumber();
		this.title = c.getTitle();
		this.description = c.getDescription();
		this.created = c.getCreationTime();
		this.modified = c.getModificationTime();
		this.createdBy = new UserSummary(c.getCreationUser());
		this.modifiedBy = new UserSummary(c.getModificationUser());
		for (Client client : c.getClients())
			clients.add(new ClientDetails(client));
		this.court = new CourtSummary(c.getCourt());
	}

	public Long getId() {
		return id;
	}

	public String getNumber() {
		return number;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Timestamp getCreated() {
		return created;
	}

	public Timestamp getModified() {
		return modified;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public UserSummary getModifiedBy() {
		return modifiedBy;
	}

	public List<ClientDetails> getClients() {
		return clients;
	}

	public CourtSummary getCourt() {
		return court;
	}

}