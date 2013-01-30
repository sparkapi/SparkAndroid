package com.sparkplatform.api.services; 

import java.util.HashMap;
import java.util.List;

import com.sparkplatform.api.ApiParameter;
import com.sparkplatform.api.Client;
import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.models.PropertyType;
import com.sparkplatform.api.models.StandardField;

public class StandardFieldService extends BaseService<StandardField> {

	@Override
	public String getPath() {
		return "/standardfields";
	}

	public StandardFieldService(Client c) {
		super(c);
	}
	public List<StandardField> nearby(String latititude, String longitued, String expand, PropertyType first, PropertyType ... types) throws FlexmlsApiClientException{
		StringBuffer buffer = new StringBuffer(getPath()).append("/nearby/").append(first.getMlsCode());
		for (PropertyType propertyType : types) {
			buffer.append(",").append(propertyType.getMlsCode());
		}
		return getClient().get(buffer.toString(), new HashMap<ApiParameter, String>()).getResults(StandardField.class);
	}
	
}
