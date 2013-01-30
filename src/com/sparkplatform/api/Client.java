package com.sparkplatform.api;

import java.util.HashMap;
import java.util.Map;

import com.sparkplatform.api.services.ContactService;
import com.sparkplatform.api.services.ListingService;
import com.sparkplatform.api.services.MarketStatisticsService;
import com.sparkplatform.api.services.PropertyTypeService;
import com.sparkplatform.api.services.StandardFieldService;
import com.sparkplatform.api.services.SystemInfoService;

/**
 * Main API client interface.  This client is strictly set to use only parameters and feature set 
 * specified in the documentation.  An instance of this class is provided to all service 
 * implementations.
 * 
 * @see BaseClient
 */
public class Client extends BaseClient<ApiParameter> {
	public Client(Configuration config, Connection<Response> defaultConnection, Connection<Response> secureConnection) {
		super(config, defaultConnection, secureConnection);
	}
	public Client(Configuration config) {
		super(config);
	}
	
	@Override
	Map<String, String> stringifyParameterKeys(Map<ApiParameter, String> parms) {
		Map<String, String> strings = new HashMap<String, String>();
		for (ApiParameter parm : parms.keySet()) {
			strings.put(parm.toString(), parms.get(parm));
		}
		return strings;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	// Service registry
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	private ContactService contactService; 
	private ListingService listingService;
	private PropertyTypeService propertyTypeService;
	private MarketStatisticsService marketStatisticsService;
	private SystemInfoService systemInfoService;
	private StandardFieldService standardFieldService;
	
	public ContactService getContactService() {
		if(contactService == null){
			contactService = new ContactService(this);
		}
		return contactService;
	}

	public void setContactService(ContactService contactService) {
		this.contactService = contactService;
	}
	
	public ListingService getListingService() {
		if(listingService == null){
			listingService = new ListingService(this);
		}
		return listingService;
	}

	public void setListingService(ListingService listingService) {
		this.listingService = listingService;
	}

	public PropertyTypeService getPropertyTypeService() {
		if(propertyTypeService == null){
			propertyTypeService = new PropertyTypeService(this);
		}
		return propertyTypeService;
	}

	public void setPropertyTypeService(PropertyTypeService propertyTypeService) {
		this.propertyTypeService = propertyTypeService;
	}
	
	public MarketStatisticsService getMarketStatisticsService() {
		if(marketStatisticsService == null){
			marketStatisticsService = new MarketStatisticsService(this);
		}
		return marketStatisticsService;
	}

	public void setMarketStatisticsService(
			MarketStatisticsService marketStatisticsService) {
		this.marketStatisticsService = marketStatisticsService;
	}

	public SystemInfoService getSystemInfoService() {
		if(systemInfoService == null){
			systemInfoService = new SystemInfoService(this);
		}
		return systemInfoService;
	}

	public void setSystemInfoService(SystemInfoService systemInfoService) {
		this.systemInfoService = systemInfoService;
	}

	public StandardFieldService getStandardFieldService() {
		if(standardFieldService == null){
			standardFieldService = new StandardFieldService(this);
		}
		return standardFieldService;
	}

	public void setStandardFieldService(StandardFieldService standardFieldService) {
		this.standardFieldService = standardFieldService;
	}
	
}
