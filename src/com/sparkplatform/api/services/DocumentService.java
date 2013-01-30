package com.sparkplatform.api.services; 

import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.Document;

public class DocumentService extends SubResourceService<Document> {
	
	public DocumentService(Client c, String prefix) {
		super(c,prefix);
	}

	@Override
	public String getSubResourcePath() {
		return "/documents";
	}

	
}
