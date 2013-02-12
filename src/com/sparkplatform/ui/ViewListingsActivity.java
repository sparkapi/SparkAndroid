package com.sparkplatform.ui;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.sparkplatform.api.ApiParameter;
import com.sparkplatform.api.FlexmlsApiClientException;
import com.sparkplatform.api.Response;
import com.sparkplatform.api.SparkClient;
import com.sparkplatform.api.models.Listing;
import com.sparkplatform.utils.ListingFormatter;

public class ViewListingsActivity extends ListActivity implements SearchView.OnQueryTextListener {
	
	// class vars *************************************************************
	
	private static final String TAG = "ViewListingsActivity";
	
	private static final String DEFAULT_FILTER = "PropertyType Eq 'A'";
	
	// instance vars **********************************************************
	
	private List<Listing> listings;
	private SearchView searchView;
	
	// interface **************************************************************
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_listings);
		
		   new SearchListingsTask().execute(DEFAULT_FILTER);
		   
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
		
	    this.searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
	    searchView.setQuery(DEFAULT_FILTER, false);
	    searchView.setOnQueryTextListener(this);
		
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
	    			 map.put("image", ListingFormatter.getListingPrimaryPhoto(l));
	    			 map.put("line1", ListingFormatter.getListingTitle(l));
	    			 map.put("line2", ListingFormatter.getListingSubtitle(l));
	    			 list.add(map);
	    			 
	    			 ImageLoaderTask imageLoaderTask = new ImageLoaderTask();
	    			 imageLoaderTask.imageView = null;
	    		 }

	    		 ListAdapter adapter = new SimpleImageAdapter(getApplicationContext(), 
	    				 list,
	    				 R.layout.image_two_line_list_item, 
	    				 new String[] {"image", "line1", "line2"}, 
	    				 new int[] {R.id.icon, R.id.line1, R.id.line2});
	    		 setListAdapter(adapter);
	    		 
	    		 //setListAdapter(new ListingAdapter(getApplicationContext(),listings));
	    	 } catch (FlexmlsApiClientException e) {
	    		 Log.e(TAG,"Listing JSON binding exception", e);
	    	 }
		 }
	 }
	 
	 public class SimpleImageAdapter extends SimpleAdapter
	 {
		 public SimpleImageAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
		 {
			 super(context,data,resource,from,to);
		 }
		 
		 public void setViewImage(ImageView v, String value) {
			 ImageLoaderTask task = new ImageLoaderTask();
			 task.imageView = v;
			 task.execute(value);
		 }
	 }
	 
	 private class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
		 
		 private ImageView imageView;
		 
	     protected Bitmap doInBackground(String... urlString) {

	    	 try
	    	 {
	    		 URL url = new URL(urlString[0]);
	    		 URLConnection conn = url.openConnection();
	    		 conn.connect();
	    		 InputStream inputStream = conn.getInputStream();
	    		 Bitmap bm = BitmapFactory.decodeStream(inputStream);
	    		 return bm;
	    	 }
	    	 catch(Exception e)
	    	 {
	    		 Log.e(TAG, "ImageLoaderTask", e);
	    		 return null;
	    	 }
	     }
	     
	     protected void onPostExecute(Bitmap bitmap) {
	    	 if(bitmap != null)
	    		 imageView.setImageBitmap(bitmap);
	     }
	 }
	 
	 // OnQueryTextListener ***************************************************
	 
	 public boolean onQueryTextChange (String newText)
	 {
		 return false;
	 }
	 
	 public boolean onQueryTextSubmit (String query)
	 {
		 this.searchView.clearFocus();
		 new SearchListingsTask().execute(query);
		 return true;
	 }
	
}
