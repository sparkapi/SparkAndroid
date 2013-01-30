package com.sparkplatform.api.services; 

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.Video;

public class VideoService extends SubResourceService<Video> {
	
	public VideoService(Client c, String prefix) {
		super(c,prefix);
	}

	@Override
	public String getSubResourcePath() {
		return "/videos";
	}

	
}
