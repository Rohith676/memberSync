package com.aegis.membersync.model;

public class MemberRequest {

	private String requestNumber;
	private String status;
	private String closedBy;
	private String closedAt;

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	public String getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(String closedAt) {
		this.closedAt = closedAt;
	}

}