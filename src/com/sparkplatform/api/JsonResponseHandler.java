package com.sparkplatform.api;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * JSON client parser for the HTTP response.
 */
public class JsonResponseHandler implements ResponseHandler<Response> {
	// TODO When jackson 1.8 is released, investigate using the 
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public Response handleResponse(HttpResponse response) {
		Response r;
		try {
			InputStream content = response.getEntity().getContent();
			int statusCode = response.getStatusLine().getStatusCode();
			r = parseResponse(content, statusCode);
		} catch (IOException e) {
			r = new Response(new FlexmlsApiClientException("Failure parsing JSON resonse.  The server response may be invalid", e));
		}
		return r;
	}

	/**
	 * Parse the response JSON into the standard API response object.
	 * @param content response content
	 * @param statusCode response HTTP status
	 * @return A response object
	 * @throws IOException
	 */
	public Response parseResponse(InputStream content, int statusCode) throws IOException { 
		JsonNode root;
		Response r = null;
		root = mapper.readValue(content, JsonNode.class);
		r = parse(root, statusCode);
		return r;
	}
	
	private Response parse(JsonNode root, int status){
		 // can reuse, share globally
		JsonNode rootNode = root.get("D");
		Response r = new Response(mapper, rootNode);
		r.setSuccess(rootNode.get("Success").getValueAsBoolean());
		r.setStatus(status);
		if(!r.isSuccess()){
			r.setCode(rootNode.get("Code").getValueAsInt());
			r.setMessage(rootNode.get("Message").getValueAsText());
		}
		// TODO pagination
		return r;		
	}
}
