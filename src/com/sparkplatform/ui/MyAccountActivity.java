package com.sparkplatform.ui;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.Response;
import com.sparkplatform.api.SparkClient;

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
	    	 Log.d(TAG,r.getResultsJSONString());
	    	 
	    	 /*
	    	 try {
	    		 List<Listing> listings = r.getResults(Listing.class);

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
	    	 */

	     }
	 }
}
