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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.sparkplatform.api.SparkAPI;
import com.sparkplatform.api.SparkAPIClientException;
import com.sparkplatform.api.core.Response;

public class MyAccountActivity extends ListActivity {
	
	private static final String TAG = "MyAccountActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		getListView().setEmptyView(findViewById(R.id.myAccountProgressBar));

		if(SparkAPI.getInstance().isHybridSession())
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
			SecurePreferences p = new SecurePreferences(this,UIConstants.SPARK_PREFERENCES, SparkAPI.getConfiguration().getApiSecret(), false);
			p.removeValue(UIConstants.AUTH_ACCESS_TOKEN);
			p.removeValue(UIConstants.AUTH_REFRESH_TOKEN);
			p.removeValue(UIConstants.AUTH_OPENID);
			
			p.removeValue(UIConstants.PROPERTY_OPENID_ID);
			p.removeValue(UIConstants.PROPERTY_OPENID_FRIENDLY);
			p.removeValue(UIConstants.PROPERTY_OPENID_FIRST_NAME);
			p.removeValue(UIConstants.PROPERTY_OPENID_MIDDLE_NAME);
			p.removeValue(UIConstants.PROPERTY_OPENID_LAST_NAME);
			p.removeValue(UIConstants.PROPERTY_OPENID_EMAIL);
			
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
	    		 r = SparkAPI.getInstance().get("/my/account",null);
	    	 }
	    	 catch(SparkAPIClientException e)
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
		SecurePreferences p = new SecurePreferences(this,UIConstants.SPARK_PREFERENCES, SparkAPI.getConfiguration().getApiSecret(), false);
		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		 ActivityHelper.addListLine(list, "ID", p.getString(UIConstants.PROPERTY_OPENID_ID));
		 ActivityHelper.addListLine(list, "Full Name", p.getString(UIConstants.PROPERTY_OPENID_FRIENDLY));
		 ActivityHelper.addListLine(list, "First Name", p.getString(UIConstants.PROPERTY_OPENID_FIRST_NAME));
		 ActivityHelper.addListLine(list, "Middle Name", p.getString(UIConstants.PROPERTY_OPENID_MIDDLE_NAME));
		 ActivityHelper.addListLine(list, "Last Name", p.getString(UIConstants.PROPERTY_OPENID_LAST_NAME));
		 ActivityHelper.addListLine(list, "Email", p.getString(UIConstants.PROPERTY_OPENID_EMAIL));

		 ListAdapter adapter = new SimpleAdapter(getApplicationContext(), 
				 list,
				 R.layout.two_line_list_item, 
				 new String[] {"line1", "line2"}, 
				 new int[] {android.R.id.text1, android.R.id.text2});
		 setListAdapter(adapter);
	 }
}
