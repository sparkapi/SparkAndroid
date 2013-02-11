package com.sparkplatform.ui;

import java.util.ArrayList;
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

public class MyAccountActivity extends ListActivity {
	
	private static final String TAG = "MyAccountActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		if(SparkClient.getInstance().isHybridSession())
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
	    				 android.R.layout.two_line_list_item, 
	    				 new String[] {"line1", "line2"}, 
	    				 new int[] {android.R.id.text1, android.R.id.text2});
	    		 setListAdapter(adapter);
	    	 }
	     }
	 }
	 
	 private void buildOpenIDListAdapter()
	 {
		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		 ActivityHelper.addListLine(list, "ID", System.getProperty(UIConstants.PROPERTY_OPENID_ID));
		 ActivityHelper.addListLine(list, "Full Name", System.getProperty(UIConstants.PROPERTY_OPENID_FRIENDLY));
		 ActivityHelper.addListLine(list, "First Name", System.getProperty(UIConstants.PROPERTY_OPENID_FIRST_NAME));
		 ActivityHelper.addListLine(list, "Middle Name", System.getProperty(UIConstants.PROPERTY_OPENID_MIDDLE_NAME));
		 ActivityHelper.addListLine(list, "Last Name", System.getProperty(UIConstants.PROPERTY_OPENID_LAST_NAME));
		 ActivityHelper.addListLine(list, "Email", System.getProperty(UIConstants.PROPERTY_OPENID_EMAIL));

		 ListAdapter adapter = new SimpleAdapter(getApplicationContext(), 
				 list,
				 android.R.layout.two_line_list_item, 
				 new String[] {"line1", "line2"}, 
				 new int[] {android.R.id.text1, android.R.id.text2});
		 setListAdapter(adapter);
	 }
}
