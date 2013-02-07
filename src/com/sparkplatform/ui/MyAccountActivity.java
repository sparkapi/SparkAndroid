package com.sparkplatform.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.Response;
import com.sparkplatform.api.SparkClient;
import com.sparkplatform.api.models.Listing;
import com.sparkplatform.utils.ListingFormatter;

public class MyAccountActivity extends ListActivity {
	
	private static final String TAG = "MyAccountActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		new MyAccountTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_my_account, menu);
		return true;
	}
	
	 private class MyAccountTask extends AsyncTask<Void, Void, Response> {
	     protected Response doInBackground(Void... v) {
				   
	    	 Response r = null;
	    	 try
	    	 {
	    		 r = SparkClient.getInstance().get("/my/account",null);
	    		 Log.d(TAG, "success>" + r.isSuccess());
	    	 }
	    	 catch(FlexmlsApiClientException e)
	    	 {
	    		 Log.e(TAG, "/my/account exception>", e);
	    	 }
	    	 
	    	 return r;
	     }
	     	     
	     protected void onPostExecute(Response r) {
	    	 JsonNode results = r.getResultsJSON();
	    	 if(results != null && results.isArray() && results.size() > 0)
	    	 {
	    		 JsonNode account = results.get(0);
	    		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	    		 addAccountLine(list, "Name", account.get("Name").getTextValue());
	    		 addAccountLine(list, "Office", account.get("Office").getTextValue());
	    		 addAccountLine(list, "Company", account.get("Company").getTextValue());
	    		 addArrayLine(account, "Addresses", "Address", list, "Address");
	    		 addAccountLine(list, "MLS", account.get("Mls").getTextValue());
	    		 addArrayLine(account, "Emails", "Address", list, "Email");
	    		 addArrayLine(account, "Phones", "Number", list, "Phone");
	    		 addArrayLine(account, "Websites", "Uri", list, "Website");
	    		 
	    		 ListAdapter adapter = new SimpleAdapter(getApplicationContext(), 
	    				 list,
	    				 android.R.layout.two_line_list_item, 
	    				 new String[] {"line1", "line2"}, 
	    				 new int[] {android.R.id.text1, android.R.id.text2});
	    		 setListAdapter(adapter);
	    	 }
	     }
	 }
	 
	 private void addAccountLine(List<Map<String,String>> list, String key, String value)
	 {
		 Map<String,String> map = new HashMap<String,String>();
		 map.put("line1", value);
		 map.put("line2", key);
		 list.add(map);
	 }
	 
	 private void addArrayLine(JsonNode account, String arrayKey, String itemKey, List<Map<String,String>> list, String key)
	 {
		 JsonNode array = account.get(arrayKey);
		 if(array != null && array.isArray() && array.size() > 0)
		 {
			 JsonNode firstItem = array.get(0);
			 addAccountLine(list, key, firstItem.get(itemKey).getTextValue());
		 }
	 }
}
