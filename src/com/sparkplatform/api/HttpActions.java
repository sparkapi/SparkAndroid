package com.sparkplatform.api;

import java.util.Map;

/**
 * All the supported HTTP actions
 * @author wade
 *
 * @param <T> Type of response object returned
 * @param <U> Parameter key type for request parameters
 */
public interface HttpActions<T, U> {
	/**
	 * HTTP Get
	 * @param path request path
	 * @param options request parameters
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	T get(String path, Map<U, String> options) throws FlexmlsApiClientException;
	/**
	 * HTTP Delete
	 * @param path request path
	 * @param options request parameters
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	T delete(String path, Map<U, String> options)throws FlexmlsApiClientException;
	/**
	 * HTTP Post
	 * @param path request path
	 * @param body post data
	 * @param options request parameters
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	T post(String path, String body, Map<U, String> options)throws FlexmlsApiClientException;
	/**
	 * HTTP Put
	 * @param path request path
	 * @param body post data
	 * @param options request parameters
	 * @return Response object
	 * @throws FlexmlsApiClientException
	 */
	T put(String path, String body, Map<U, String> options)throws FlexmlsApiClientException;
}
