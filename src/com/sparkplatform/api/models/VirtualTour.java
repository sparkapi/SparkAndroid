package com.sparkplatform.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class VirtualTour extends ResourceEntity {
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Uri")
	private String uri;
	@JsonProperty("Type")
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
