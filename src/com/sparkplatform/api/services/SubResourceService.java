package com.sparkplatform.api.services;

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.Base;

public abstract class SubResourceService<T extends Base> extends BaseService<T> {

	private String prefix = "";

	public SubResourceService(Client c, String prefix) {
		super(c);
		this.prefix = prefix;
	}

	@Override
	public String getPath() {
		return getPrefix() + getSubResourcePath();
	}
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public abstract String getSubResourcePath();

}