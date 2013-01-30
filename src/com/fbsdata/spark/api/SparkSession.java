package com.fbsdata.spark.api;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import com.flexmls.flexmls_api.FlexmlsApiClientException;
import com.flexmls.flexmls_api.Session;

@JsonIgnoreProperties({"expires_in"})

public class SparkSession extends Session {
	@JsonProperty("access_token")
	private String accessToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public boolean isExpired(){
		return accessToken == null || refreshToken == null;
	}
	
	Session authenticate() throws FlexmlsApiClientException {
		throw new FlexmlsApiClientException("Spark authentication required");
	}
	
}
