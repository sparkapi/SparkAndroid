//
//  Copyright (c) 2013 Financial Business Systems, Inc. All rights reserved.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

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
