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

package com.sparkplatform.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.sparkplatform.api.SparkAPIClientException;

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

	 public static void errorDialog(String errorMessage, SparkAPIClientException exception, Context context, final View viewToHide)
	 {
		 StringBuilder errorBuilder = new StringBuilder();
		 errorBuilder.append("Error: ");
		 if(errorMessage != null)
			 errorBuilder.append(errorMessage);
		 if(exception != null)
			 errorBuilder.append(exception.getClass().getName() + "- " + (exception.getMessage() != null ? exception.getMessage() : ""));

		 AlertDialog.Builder builder = new AlertDialog.Builder(context);
		 builder.setMessage(errorBuilder.toString())
		 .setNeutralButton(R.string.button_OK, new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int id) {
				 if(viewToHide != null)
					 viewToHide.setVisibility(View.INVISIBLE);
			 }
		 }).show();
	 }
}
