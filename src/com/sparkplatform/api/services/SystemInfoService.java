package com.sparkplatform.api.services; 

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.SystemInfo;

public class SystemInfoService extends BaseService<SystemInfo> {

	@Override
	public String getPath() {
		return "/system";
	}

	public SystemInfoService(Client c) {
		super(c);
	}
	
}
