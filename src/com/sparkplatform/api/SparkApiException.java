package com.sparkplatform.api;

/**
 * Errors Specifically returned from the API
 */
public class SparkApiException extends FlexmlsApiClientException {
	private static final long serialVersionUID = -8156427208964545915L;
	private String message;
	private ApiCode code;
	private int status;
	public SparkApiException(String message, ApiCode code, int status) {
		super(message);
		this.message = message;
		this.code = code;
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public ApiCode getCode() {
		return code;
	}
	public int getStatus() {
		return status;
	}
}
