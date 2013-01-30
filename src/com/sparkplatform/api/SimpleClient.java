package com.sparkplatform.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Unrestricted client for doing more experimental queries.  This client is unsupported by the 
 * service classes provided by the library, but can be used for hitting the services directly in a 
 * more scripted manner.
 */
public class SimpleClient extends BaseClient<String> {
	public SimpleClient(Configuration config, Connection<Response> defaultConnection, Connection<Response> secureConnection) {
		super(config, defaultConnection, secureConnection);
	}
	public SimpleClient(Configuration config) {
		super(config);
	}	
	@Override
	Map<String, String> stringifyParameterKeys(Map<String, String> parms) {
		return new HashMap<String, String>(parms);
	}
	
}
