package com.sparkplatform.api.services; 


import com.sparkplatform.api.Client;
import com.sparkplatform.api.models.Contact;

public class ContactService extends BaseService<Contact> {

	@Override
	public String getPath() {
		return "/contacts";
	}

	public ContactService(Client c) {
		super(c);
	}
	
}
