package com.sparkplatform.api.services; 

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.OpenHouse;

public class OpenHouseService extends SubResourceService<OpenHouse> {
	
	public OpenHouseService(Client c, String prefix) {
		super(c,prefix);
	}

	@Override
	public String getSubResourcePath() {
		return "/openhouses";
	}

	
}
