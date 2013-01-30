package com.sparkplatform.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Contact extends ResourceEntity {
	
	@JsonProperty("DisplayName")
	private String displayName;
	@JsonProperty("PrimaryEmail")
	private String primaryEmail;
	
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getPrimaryEmail() {
		return primaryEmail;
	}
	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}
	
}
