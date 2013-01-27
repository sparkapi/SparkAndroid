package com.fbsdata.spark.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.log4j.Logger;

import com.flexmls.flexmls_api.Client;
import com.flexmls.flexmls_api.Configuration;
import com.flexmls.flexmls_api.Connection;
import com.flexmls.flexmls_api.Response;

public class SparkClient extends Client {

	// constants **************************************************************
	
	public static final String sparkClientKey = "<YOUR OAUTH2 CLIENT KEY>";
	public static final String sparkClientSecret = "<YOUR OAUTH2 CLIENT SECRET>";
	public static final String sparkCallbackURL = "https://sparkplatform.com/oauth2/callback";
	
	public static final String sparkOpenIdLogoutURL = "https://sparkplatform.com/openid/logout";
	
	public static final String sparkAPIEndpoint = "https://sparkapi.com/";
	public static final String sparkAPIVersion = "/v1";
	public static final String sparkOAuth2Grant = "/oauth2/grant";
	
	// class vars *************************************************************
	
	private static Logger logger = Logger.getLogger(SparkClient.class);
	
	// constructors ***********************************************************
	
	public SparkClient(Configuration config) {
		super(config);
	}
	
	public SparkClient(Configuration config, Connection<Response> defaultConnection, Connection<Response> secureConnection) {
		super(config, defaultConnection, secureConnection);
	}

	// interface **************************************************************
	
	public String getSparkOpenIdURLString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("https://sparkplatform.com/openid");
		builder.append("?openid.mode=checkid_setup");
		builder.append("&openid.spark.client_id=");
		try {
			builder.append(URLEncoder.encode(getConfig().getApiKey(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		builder.append("&openid.return_to=");
		try {
			builder.append(URLEncoder.encode(sparkCallbackURL, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		return builder.toString();
	}

	public String getSparkHybridOpenIdURLString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getSparkOpenIdURLString());
		builder.append("&openid.spark.combined_flow=true");
		return builder.toString();
	}

	public static String getSparkOAuth2GrantString()
	{
		return sparkAPIEndpoint + sparkAPIVersion + sparkOAuth2Grant;
	}
	
	public static void initSparkHeader(HttpUriRequest httpRequest)
	{		
		httpRequest.setHeader("User-Agent","DreamCommerce Spark Java API 0.1");
		httpRequest.setHeader("X-SparkApi-User-Agent","DreamCommerce SparkClient 0.1");
	}
	
	/*
	public boolean hybridAuthenticate(String url, SparkClient sparkClient)
		// throws SparkException
	{
		
	}
	*/
}
