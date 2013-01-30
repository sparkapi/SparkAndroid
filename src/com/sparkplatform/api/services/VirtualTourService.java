package com.sparkplatform.api.services; 

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.VirtualTour;

public class VirtualTourService extends SubResourceService<VirtualTour> {
	
	public VirtualTourService(Client c, String prefix) {
		super(c,prefix);
	}

	@Override
	public String getSubResourcePath() {
		return "/virtualtours";
	}

	
}
