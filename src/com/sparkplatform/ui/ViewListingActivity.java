package com.sparkplatform.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.sparkplatform.api.ApiParameter;
import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.Response;
import com.sparkplatform.api.SparkClient;
import com.sparkplatform.api.models.Listing;
import com.sparkplatform.utils.ListingFormatter;

public class ViewListingActivity extends ListActivity {

	// class vars *************************************************************
	
	private static final String TAG = "ViewListingActivity";
	
	// instance vars **********************************************************
	
	private Listing summaryListing;
	
	// interface **************************************************************
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_listing);
		
		Intent i = getIntent();
		summaryListing = (Listing)i.getSerializableExtra(UIConstants.EXTRA_LISTING);
		
    	Log.d(TAG, "summaryListing>" + summaryListing);
    	
		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			 Map<String,String> map = new HashMap<String,String>();
			 map.put("line1", ListingFormatter.getListingTitle(summaryListing));
			 map.put("line2", ListingFormatter.getListingSubtitle(summaryListing));
			 list.add(map);

		 ListAdapter adapter = new SimpleAdapter(getApplicationContext(), 
				 list,
				 android.R.layout.two_line_list_item, 
				 new String[] {"line1", "line2"}, 
				 new int[] {android.R.id.text1, android.R.id.text2});
		 setListAdapter(adapter);

		 new ViewListingTask().execute();
		 new StandardFieldsTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_listing, menu);
		return true;
	}
	
	 private class ViewListingTask extends AsyncTask<Void, Void, Response> {
	     protected Response doInBackground(Void... v) {
				   
	    	 Map<ApiParameter,String> parameters = new HashMap<ApiParameter,String>();
	    	 parameters.put(ApiParameter._limit, "1");
	    	 parameters.put(ApiParameter._expand, "Photos");
	    	 parameters.put(ApiParameter._filter, "ListingId Eq '" + summaryListing.getStandardFields().getListingId() + "'");
	    	 
	    	 Response r = null;
	    	 try
	    	 {
	    		 r = SparkClient.getInstance().get("/listings",parameters);
	    		 Log.d(TAG, "success>" + r.isSuccess());
	    	 }
	    	 catch(FlexmlsApiClientException e)
	    	 {
	    		 Log.e(TAG, "/listings exception>", e);
	    	 }
	    	 
	    	 return r;
	     }
	     
	     protected void onPostExecute(Response r) {
	    	 Log.d(TAG,"/listings>" + r.getResultsJSONString());
		 }
	 }
	 
	 private class StandardFieldsTask extends AsyncTask<Void, Void, Response> {
		 protected Response doInBackground(Void... v) {

			 Response r = null;
			 try
			 {
				 r = SparkClient.getInstance().get("/standardfields", null);
				 Log.d(TAG, "success>" + r.isSuccess());
			 }
			 catch(FlexmlsApiClientException e)
			 {
				 Log.e(TAG, "/standardfields exception>", e);
			 }

			 return r;
		 }

		 protected void onPostExecute(Response r) {
			 Log.d(TAG,"/standardfields>" + r.getResultsJSONString());

		 }
	 }

}
