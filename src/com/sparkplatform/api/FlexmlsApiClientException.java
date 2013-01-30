package com.sparkplatform.api;

/**
 * General client exceptions
 */
public class FlexmlsApiClientException extends Exception {
	private static final long serialVersionUID = 2689086275082482561L;

	public FlexmlsApiClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public FlexmlsApiClientException(String message) {
		super(message);
	}

}
