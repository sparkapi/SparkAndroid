package com.sparkplatform.api;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP Abstraction layer for the API.  Implementations are responsible for performing the HTTP 
 * request, and parsing the resulting JSON response into some thing the client can make use of.
 * The implementation should also raise API exceptions when the API response is unsuccessful.
 *
 * @param <R> The response object
 */
public abstract class Connection<R> implements HttpActions<R, String> {
	// TODO Make this truly read only.
	private static final Map<String, String> EMPT_MAP = new HashMap<String, String>(); 
	
	/**
	 * Helper get with no parameters
	 * @param path request path
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	public R get(String path) throws FlexmlsApiClientException {
		return get(path, EMPT_MAP);
	}
	/**
	 * Helper get with no parameters
	 * @param path request path
	 * @param body post data
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	public R post(String path, String body) throws FlexmlsApiClientException {
		return post(path, body, EMPT_MAP);
	}
	/**
	 * Helper get with no parameters
	 * @param path request path
	 * @param body post data
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	public R put(String path, String body) throws FlexmlsApiClientException {
		return put(path, body, EMPT_MAP);
	}
	/**
	 * Helper delete with no parameters
	 * @param path request path
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	public R delete(String path) throws FlexmlsApiClientException {
		return delete(path, EMPT_MAP);
	}
}
