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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.sparkplatform.api.SparkApiClientException;
import com.sparkplatform.api.SparkApi;
import com.sparkplatform.api.core.Response;

public class MyAccountActivity extends ListActivity {
	
	private static final String TAG = "MyAccountActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		getListView().setEmptyView(findViewById(R.id.myAccountProgressBar));

		if(SparkApi.getInstance().isHybridSession())
			new MyAccountTask().execute();
		else
			buildOpenIDListAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_account, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		if(item.getItemId() == R.id.menu_logout)
		{
			// remove shared preferences
			SharedPreferences p = getSharedPreferences(UIConstants.SPARK_PREFERENCES, MODE_PRIVATE);
			SharedPreferences.Editor editor = p.edit();
			editor.remove(UIConstants.AUTH_ACCESS_TOKEN);
			editor.remove(UIConstants.AUTH_REFRESH_TOKEN);
			editor.remove(UIConstants.AUTH_OPENID);
			
			editor.remove(UIConstants.PROPERTY_OPENID_ID);
			editor.remove(UIConstants.PROPERTY_OPENID_FRIENDLY);
			editor.remove(UIConstants.PROPERTY_OPENID_FIRST_NAME);
			editor.remove(UIConstants.PROPERTY_OPENID_MIDDLE_NAME);
			editor.remove(UIConstants.PROPERTY_OPENID_LAST_NAME);
			editor.remove(UIConstants.PROPERTY_OPENID_EMAIL);
			editor.commit();
			
			// pop to login
    		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		startActivity(intent);
			
			return true;
		}
		
		return false;
	}
	
	 private class MyAccountTask extends AsyncTask<Void, Void, Response> {
	     protected Response doInBackground(Void... v) {
				   
	    	 Response r = null;
	    	 try
	    	 {
	    		 r = SparkApi.getInstance().get("/my/account",null);
	    	 }
	    	 catch(SparkApiClientException e)
	    	 {
	    		 Log.e(TAG, "/my/account exception>", e);
	    	 }
	    	 
	    	 return r;
	     }
	     	     
	     protected void onPostExecute(Response r) {
	    	 JsonNode account = r.getFirstResult();
	    	 
	    	 if(account != null)
	    	 {
	    		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	    		 ActivityHelper.addListLine(list, "Name", account.get("Name").getTextValue());
	    		 ActivityHelper.addListLine(list, "Office", account.get("Office").getTextValue());
	    		 ActivityHelper.addListLine(list, "Company", account.get("Company").getTextValue());
	    		 ActivityHelper.addArrayLine(account, "Addresses", "Address", list, "Address");
	    		 ActivityHelper.addListLine(list, "MLS", account.get("Mls").getTextValue());
	    		 ActivityHelper.addArrayLine(account, "Emails", "Address", list, "Email");
	    		 ActivityHelper.addArrayLine(account, "Phones", "Number", list, "Phone");
	    		 ActivityHelper.addArrayLine(account, "Websites", "Uri", list, "Website");

	    		 ListAdapter adapter = new SimpleAdapter(getApplicationContext(), 
	    				 list,
	    				 R.layout.two_line_list_item, 
	    				 new String[] {"line1", "line2"}, 
	    				 new int[] {android.R.id.text1, android.R.id.text2});
	    		 setListAdapter(adapter);
	    	 }
	     }
	 }
	 
	 private void buildOpenIDListAdapter()
	 {
		SharedPreferences p = getSharedPreferences(UIConstants.SPARK_PREFERENCES, MODE_PRIVATE);
		 
		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		 ActivityHelper.addListLine(list, "ID", p.getString(UIConstants.PROPERTY_OPENID_ID, null));
		 ActivityHelper.addListLine(list, "Full Name", p.getString(UIConstants.PROPERTY_OPENID_FRIENDLY, null));
		 ActivityHelper.addListLine(list, "First Name", p.getString(UIConstants.PROPERTY_OPENID_FIRST_NAME, null));
		 ActivityHelper.addListLine(list, "Middle Name", p.getString(UIConstants.PROPERTY_OPENID_MIDDLE_NAME, null));
		 ActivityHelper.addListLine(list, "Last Name", p.getString(UIConstants.PROPERTY_OPENID_LAST_NAME, null));
		 ActivityHelper.addListLine(list, "Email", p.getString(UIConstants.PROPERTY_OPENID_EMAIL, null));

		 ListAdapter adapter = new SimpleAdapter(getApplicationContext(), 
				 list,
				 R.layout.two_line_list_item, 
				 new String[] {"line1", "line2"}, 
				 new int[] {android.R.id.text1, android.R.id.text2});
		 setListAdapter(adapter);
	 }
}
