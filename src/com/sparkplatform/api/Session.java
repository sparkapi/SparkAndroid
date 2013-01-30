package com.sparkplatform.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Essentially read only, strictly validated JSON entity for the API session.
 */
public class Session {
	@JsonProperty("AuthToken")
	private String token;
	@JsonProperty("Roles")
	private List<String> roles = new ArrayList<String>();
	@JsonProperty("Expires")
	private Date expiration;
	
	public Session() { }
	
	public Session(String token, List<String> roles, Date expiration) {
		super();
		this.token = token;
		this.roles = Collections.unmodifiableList(roles);
		this.expiration = expiration;
	}
	public String getToken() {
		return token;
	}
	public List<String> getRoles() {
		return roles;
	}
	public Date getExpiration() {
		return expiration;
	}
	
	public boolean isExpired(){
		return expiration.before(new Date());
	}
}
