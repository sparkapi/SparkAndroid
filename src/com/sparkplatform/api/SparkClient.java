package com.sparkplatform.api;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

public class SparkClient extends Client {

	// constants **************************************************************
	
	public static final String sparkClientKey = "<YOUR OAUTH2 CLIENT KEY>";
	public static final String sparkClientSecret = "<YOUR OAUTH2 CLIENT SECRET>";
	public static final String sparkCallbackURL = "https://sparkplatform.com/oauth2/callback";
	
	public static final String sparkOpenIdLogoutURL = "https://sparkplatform.com/openid/logout";
	
	public static final String sparkAPIEndpoint = "sparkapi.com";
	public static final String sparkAPIVersion = "/v1";
	public static final String sparkOAuth2Grant = "/oauth2/grant";
	
	// class vars *************************************************************
	
	private static final String TAG = "SparkClient";
	private static Logger logger = Logger.getLogger(SparkClient.class);
	
	private static SparkClient instance = null;
	
	// class interface ********************************************************

	public static SparkClient getInstance()
	{
		if(instance == null)
		{
		    Configuration c = new Configuration();
		    c.setApiKey(SparkClient.sparkClientKey);
		    c.setEndpoint(SparkClient.sparkAPIEndpoint);
		    c.setSsl(true);
		    instance = new SparkClient(c);
		}
		return instance;
	}
	
	private SparkClient(Configuration config) {
		super(config);
	}
	
	private SparkClient(Configuration config, Connection<Response> defaultConnection, Connection<Response> secureConnection) {
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
		return "https://" + sparkAPIEndpoint + sparkAPIVersion + sparkOAuth2Grant;
	}
	
	public static String isHybridAuthorized(String url)
	{
		List<NameValuePair> params;
		try {
			params = URLEncodedUtils.parse(new URI(url), "UTF-8");
		} catch (URISyntaxException e) {
			logger.error("malformed URL", e);
			return null;
		}
		
		String openIdMode = null;
		String openIdSparkCode = null;
		
	    return ((openIdMode = getParameter(params,"openid.mode")) != null &&
	       openIdMode.equals("id_res") &&
	       (openIdSparkCode = getParameter(params,"openid.spark.code")) != null) ? openIdSparkCode : null;
	}
	
	private static String getParameter(List<NameValuePair> params, String name)
	{
		for(NameValuePair nameValuePair : params)
			if(nameValuePair.getName().equals(name))
				return nameValuePair.getValue();
		
		return null;
	}
	
	public SparkSession hybridAuthenticate(String openIdSparkCode)
//		throws SparkException
	{
		   Map<String,String> map = new HashMap<String,String>();
		   map.put("client_id", SparkClient.sparkClientKey);
		   map.put("client_secret", SparkClient.sparkClientSecret);
		   map.put("grant_type", "authorization_code");
		   map.put("code", openIdSparkCode);
		   map.put("redirect_uri", SparkClient.sparkCallbackURL);
		   
		   SparkSession sparkSession = null;
		   try
		   {
			   HttpPost post = new HttpPost(SparkClient.getSparkOAuth2GrantString());
			   SparkClient.initSparkHeader(post);
			   ObjectMapper mapper = new ObjectMapper();
			   StringEntity stringEntity = new StringEntity(mapper.writeValueAsString(map));
			   stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
			   post.setEntity(stringEntity);
			   HttpClient httpclient = new DefaultHttpClient(); 
			   HttpResponse response = httpclient.execute(post);
			   String responseBody = EntityUtils.toString(response.getEntity());
			   logger.debug("OAuth2 response>" + responseBody);
			   sparkSession = mapper.readValue(responseBody, SparkSession.class);
			   setSession(sparkSession);
			   Connection<Response> connection = getConnection();
			   ((ConnectionApacheHttp)connection).setHeaders(getHeaders());
		   } 
		   catch (Exception e)
		   {
			   logger.error("exception>", e);
		   }
		   
		   return sparkSession;
	}

	public SparkSession openIdAuthenticate(String url)
		//throws SparkException
	{
		List<NameValuePair> params;
		try {
			params = URLEncodedUtils.parse(new URI(url), "UTF-8");
		} catch (URISyntaxException e) {
			logger.error("malformed URL", e);
			return null;
		}
		
		return null;
	}
	
	
	public static void initSparkHeader(HttpUriRequest httpRequest)
	{		
		httpRequest.setHeader("User-Agent","DreamCommerce Spark Java API 0.1");
		httpRequest.setHeader("X-SparkApi-User-Agent","DreamCommerce SparkClient 0.1");
	}
	
	public Map<String,String> getHeaders()
	{
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("User-Agent", "DreamCommerce Spark Java API 0.1");
		headers.put("X-SparkApi-User-Agent", "DreamCommerce SparkClient 0.1");
		if(getSession() != null)
			headers.put("Authorization", "OAuth " + ((SparkSession)getSession()).getAccessToken());
		return headers;
	}
	
	protected String requestPath(String path, Map<String, String> params) {
		StringBuilder b = new StringBuilder();
		b.append("/").append(getConfig().getVersion()).append(path).append("?");
		for (String key : params.keySet()) {
			b.append("&").append(key).append("=").append(encode(params.get(key)));
		}
		return b.toString();
	}
	
	protected String setupRequest(String path, String body, Map<String, String> options) {
		Map<String, String> params = new HashMap<String,String>();
		if(options != null)
			params.putAll(options);
		return requestPath(path, params);
	}
	
}
