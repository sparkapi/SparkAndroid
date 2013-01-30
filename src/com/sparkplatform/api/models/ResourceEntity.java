package com.sparkplatform.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Base class for JSON results that are considered resources.  E.g. contacts, listings, and documents.
 */
public class ResourceEntity extends Base {

	@JsonProperty("Id")
	private String id;
	@JsonProperty("ResourceUri")
	private String resourceUri;

	public ResourceEntity() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResourceUri() {
		return resourceUri;
	}

	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

}