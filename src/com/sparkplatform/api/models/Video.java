package com.sparkplatform.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Video extends ResourceEntity {
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Caption")
	private String caption;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("ObjectHtml")
	private String objectHtml;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getObjectHtml() {
		return objectHtml;
	}
	public void setObjectHtml(String objectHtml) {
		this.objectHtml = objectHtml;
	}
}
