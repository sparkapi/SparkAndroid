package com.sparkplatform.api.services; 

import java.util.List;
import java.util.Map;

import com.sparkplatform.api.ApiParameter;
import com.sparkplatform.api.Client;
import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.models.Listing;

public class ListingService extends BaseService<Listing> {

	@Override
	public String getPath() {
		return "/listings";
	}

	public ListingService(Client c) {
		super(c);
	}
	
	public List<Listing> my() throws FlexmlsApiClientException {
		return my(EMPTY);
	}
	public List<Listing> my(Map<ApiParameter, String> opts) throws FlexmlsApiClientException {
		return getClient().get("/my" + getPath(), opts).getResults(model());
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////
	//
	// SubResources
	//
	// TODO: I don't love this design, especially in comparison to the ruby library.  BUT, it works 
	// for the time being.
	///////////////////////////////////////////////////////////////////////////////////////////////
	
	public DocumentService getDocumentService(Listing l){
		return new DocumentService(getClient(), getPath() + "/" + l.getId());
	}

	public PhotoService getPhotoService(Listing l){
		return new PhotoService(getClient(), getPath() + "/" + l.getId());
	}

	public VideoService getVideoService(Listing l){
		return new VideoService(getClient(), getPath() + "/" + l.getId());
	}
	public VirtualTourService getVirtualTourService(Listing l){
		return new VirtualTourService(getClient(), getPath() + "/" + l.getId());
	}

	public OpenHouseService getOpenHouseService(Listing l){
		return new OpenHouseService(getClient(), getPath() + "/" + l.getId());
	}

}
