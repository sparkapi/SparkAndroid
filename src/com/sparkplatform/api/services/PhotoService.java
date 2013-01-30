package com.sparkplatform.api.services; 

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.Photo;

public class PhotoService extends SubResourceService<Photo> {
	
	public PhotoService(Client c, String prefix) {
		super(c,prefix);
	}

	@Override
	public String getSubResourcePath() {
		return "/photos";
	}

	
}
