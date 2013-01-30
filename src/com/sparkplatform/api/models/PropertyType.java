package com.sparkplatform.api.models;

import org.codehaus.jackson.annotate.JsonProperty;

public class PropertyType extends Base {
	
	@JsonProperty("MlsName")
	private String mlsName;
	@JsonProperty("MlsCode")
	private String mlsCode;
	public String getMlsName() {
		return mlsName;
	}
	public void setMlsName(String mlsName) {
		this.mlsName = mlsName;
	}
	public String getMlsCode() {
		return mlsCode;
	}
	public void setMlsCode(String mlsCode) {
		this.mlsCode = mlsCode;
	}
	
}
