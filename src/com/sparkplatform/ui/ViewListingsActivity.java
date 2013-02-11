package com.sparkplatform.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sparkplatform.api.ApiParameter;
import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.Response;
import com.sparkplatform.api.SparkClient;
import com.sparkplatform.api.models.Listing;
import com.sparkplatform.utils.ListingFormatter;

public class ViewListingsActivity extends ListActivity {
	
	// class vars *************************************************************
	
	private static final String TAG = "ViewListingsActivity";
	
	// instance vars **********************************************************
	
	private List<Listing> listings;
	
	// interface **************************************************************
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_listings);
		
		   new SearchListingsTask().execute("PropertyType Eq 'A'");
		   
		   /*
	        // Create a progress bar to display while the list loads
	        ProgressBar progressBar = new ProgressBar(this);
	        //progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, Gravity.CENTER));
	        progressBar.setIndeterminate(true);
	        getListView().setEmptyView(progressBar);

	        // Must add the progress bar to the root of the layout
	        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
	        root.addView(progressBar);
	        */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_view_listings, menu);
		return true;
	}
	
    public void onListItemClick(ListView l, View v, int position, long id) {
    	Log.d(TAG, "ListItem index>" + id);
    	
    	Listing listing = listings.get((int)id);
	 	Intent intent = new Intent(getApplicationContext(), ViewListingActivity.class);
 	    intent.putExtra(UIConstants.EXTRA_LISTING, listing);
	    startActivity(intent);	
    }

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		if(item.getItemId() == R.id.menu_account)
		{
    		Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
    		startActivity(intent);
			
			return true;
		}
		
		return false;
	}
    
	 private class SearchListingsTask extends AsyncTask<String, Void, Response> {
	     protected Response doInBackground(String... filter) {
				   
	    	 Map<ApiParameter,String> parameters = new HashMap<ApiParameter,String>();
	    	 parameters.put(ApiParameter._limit, "50");
	    	 parameters.put(ApiParameter._expand, "PrimaryPhoto");
	    	 parameters.put(ApiParameter._select, "ListingId,StreetNumber,StreetDirPrefix,StreetName,StreetDirSuffix,StreetSuffix,BedsTotal,BathsTotal,ListPrice,City,StateOrProvince");
	    	 parameters.put(ApiParameter._filter, filter[0]);
	    	 parameters.put(ApiParameter._orderby, "-ListPrice");
	    	 
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
	    	 Log.d(TAG,r.getResultsJSONString());
	    	 
	    	 try {
	    		 listings = r.getResults(Listing.class);

    			 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
	    		 for(Listing l : listings)
	    		 {
	    			 Map<String,String> map = new HashMap<String,String>();
	    			 map.put("line1", ListingFormatter.getListingTitle(l));
	    			 map.put("line2", ListingFormatter.getListingSubtitle(l));
	    			 list.add(map);
	    		 }

	    		 ListAdapter adapter = new SimpleAdapter(getApplicationContext(), 
	    				 list,
	    				 android.R.layout.two_line_list_item, 
	    				 new String[] {"line1", "line2"}, 
	    				 new int[] {android.R.id.text1, android.R.id.text2});
	    		 setListAdapter(adapter);

	    	 } catch (FlexmlsApiClientException e) {
	    		 Log.e(TAG,"Listing JSON binding exception", e);
	    	 }

		 }
	 }
	
}
