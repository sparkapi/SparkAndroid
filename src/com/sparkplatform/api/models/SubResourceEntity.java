package com.sparkplatform.api.models;

public class SubResourceEntity extends ResourceEntity {

	private String parentEntity;
	
	public SubResourceEntity(String parentEntity) {
		super();
		this.parentEntity = parentEntity;
	}

	public String getParentEntity() {
		return parentEntity;
	}

	protected void setParentEntity(String parentEntity) {
		this.parentEntity = parentEntity;
	}
}
