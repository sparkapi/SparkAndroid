package com.sparkplatform.api.services; 

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.PropertyType;

public class PropertyTypeService extends BaseService<PropertyType> {

	@Override
	public String getPath() {
		return "/propertytypes";
	}

	public PropertyTypeService(Client c) {
		super(c);
	}
	
}
