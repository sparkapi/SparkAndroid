package com.sparkplatform.api.services; 

import java.util.Map;

import com.sparkplatform.api.ApiParameter;
import com.sparkplatform.api.Client;
import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.models.MarketStatistic;

public class MarketStatisticsService extends BaseService<MarketStatistic> {

	@Override
	public String getPath() {
		return "/marketstatistics";
	}

	public MarketStatisticsService(Client c) {
		super(c);
	}
	
	public MarketStatistic absorption() throws FlexmlsApiClientException{
		return absorption(EMPTY);
	}
	public MarketStatistic absorption(Map<ApiParameter, String> options) throws FlexmlsApiClientException{
		return stat("absorption", options);
	}

	public MarketStatistic inventory() throws FlexmlsApiClientException{
		return inventory(EMPTY);
	}
	public MarketStatistic inventory(Map<ApiParameter, String> options) throws FlexmlsApiClientException{
		return stat("inventory", options);
	}

	public MarketStatistic price() throws FlexmlsApiClientException{
		return price(EMPTY);
	}
	public MarketStatistic price(Map<ApiParameter, String> options) throws FlexmlsApiClientException{
		return stat("price", options);
	}

	public MarketStatistic ratio() throws FlexmlsApiClientException{
		return ratio(EMPTY);
	}
	public MarketStatistic ratio(Map<ApiParameter, String> options) throws FlexmlsApiClientException{
		return stat("ratio", options);
	}

	public MarketStatistic dom() throws FlexmlsApiClientException{
		return dom(EMPTY);
	}
	public MarketStatistic dom(Map<ApiParameter, String> options) throws FlexmlsApiClientException{
		return stat("dom", options);
	}

	public MarketStatistic volume() throws FlexmlsApiClientException{
		return volume(EMPTY);
	}
	public MarketStatistic volume(Map<ApiParameter, String> options) throws FlexmlsApiClientException{
		return stat("volume", options);
	}


	private MarketStatistic stat(String type, Map<ApiParameter, String> options) throws FlexmlsApiClientException{
		return getClient().get(getPath() + "/" + type, options).getResults(MarketStatistic.class).get(0);
	}
	
}
