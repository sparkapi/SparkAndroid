package com.sparkplatform.api.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;

/**
 * Top level model for managing JSON entities in the library.
 */
@SuppressWarnings("serial")
public class Base implements Serializable {
	private static Logger logger = Logger.getLogger(Base.class);
	private Map<String, Object> attributes = new HashMap<String, Object>();
	
	/**
	 * This method gets called for all fields that are not mapped to a specific java class field in
	 * inheriting models.  It's a "catch-all" for future fields, or anything that the client 
	 * missed. 
	 * @param key
	 * @param value
	 */
	@JsonAnySetter
	public void setAttribute(String key, Object value){
		if(logger.isDebugEnabled()){
			logger.debug("Added attribute: "  + key);
		}
		attributes.put(key, value);
	}

	@JsonAnyGetter
	public Object getAttribute(String key){
		return attributes.get(key);
	}

	public Map<String, Object> getAttributes(){
		return attributes;
	}
	
}
