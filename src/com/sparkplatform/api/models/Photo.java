package com.sparkplatform.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class Photo extends ResourceEntity {
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Caption")
	private String caption;	
	@JsonProperty("UriThumb")
	private String uriThumb;
	@JsonProperty("Uri300")
	private String uri300;
	@JsonProperty("Uri640")
	private String uri640;
	@JsonProperty("Uri800")
	private String uri800;
	@JsonProperty("Uri1024")
	private String uri1024;
	@JsonProperty("Uri1280")
	private String uri1280;
	@JsonProperty("UriLarge")
	private String uriLarge;
	@JsonProperty("Primary")
	private boolean primary;
	
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
	public String getUriThumb() {
		return uriThumb;
	}
	public void setUriThumb(String uriThumb) {
		this.uriThumb = uriThumb;
	}
	public String getUri300() {
		return uri300;
	}
	public void setUri300(String uri300) {
		this.uri300 = uri300;
	}
	public String getUri640() {
		return uri640;
	}
	public void setUri640(String uri640) {
		this.uri640 = uri640;
	}
	public String getUri800() {
		return uri800;
	}
	public void setUri800(String uri800) {
		this.uri800 = uri800;
	}
	public String getUri1024() {
		return uri1024;
	}
	public void setUri1024(String uri1024) {
		this.uri1024 = uri1024;
	}
	public String getUri1280() {
		return uri1280;
	}
	public void setUri1280(String uri1280) {
		this.uri1280 = uri1280;
	}
	public String getUriLarge() {
		return uriLarge;
	}
	public void setUriLarge(String uriLarge) {
		this.uriLarge = uriLarge;
	}
	public boolean isPrimary() {
		return primary;
	}
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
	
}
