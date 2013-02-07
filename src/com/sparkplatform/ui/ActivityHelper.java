package com.sparkplatform.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

public class ActivityHelper {
	 
	 public static void addListLine(List<Map<String,String>> list, String key, String value)
	 {
		 Map<String,String> map = new HashMap<String,String>();
		 map.put("line1", value);
		 map.put("line2", key);
		 list.add(map);
	 }
	 
	 public static void addArrayLine(JsonNode account, String arrayKey, String itemKey, List<Map<String,String>> list, String key)
	 {
		 JsonNode array = account.get(arrayKey);
		 if(array != null && array.isArray() && array.size() > 0)
		 {
			 JsonNode firstItem = array.get(0);
			 addListLine(list, key, firstItem.get(itemKey).getTextValue());
		 }
	 }
}
