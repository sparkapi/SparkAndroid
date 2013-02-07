package com.sparkplatform.ui;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sparkplatform.api.Connection;
import com.sparkplatform.api.ConnectionApacheHttp;
import com.sparkplatform.api.Response;
import com.sparkplatform.api.SparkClient;
import com.sparkplatform.api.SparkSession;

public class WebViewActivity extends Activity {
	
	// class vars *************************************************************
	
	private static final String TAG = "WebViewActivity";

    // instance vars **********************************************************
    
    private SparkClient sparkClient;
    
    // interface **************************************************************
    
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
	    this.sparkClient = SparkClient.getInstance();
	    
		Intent intent = getIntent();
		String loginURL = intent.getBooleanExtra(UIConstants.EXTRA_LOGIN_HYBRID, true) ? 
				sparkClient.getSparkHybridOpenIdURLString() : 
				sparkClient.getSparkOpenIdURLString();
		
		WebView webView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new SparkWebViewClient());
				
		webView.loadUrl(loginURL);
	    
		//webView.loadUrl(SparkClient.sparkOpenIdLogoutURL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_web_view, menu);
		return true;
	}
	
	private class SparkWebViewClient extends WebViewClient
	{
		/*
		public void onPageFinished (WebView view, String url)
		{
			if(url.equals(SparkClient.sparkOpenIdLogoutURL))
			{
			    Configuration c = new Configuration();
			    c.setApiKey("cx8re5r5jqh5w2uqbxg7aymb8");
			    SparkClient sparkAPI = new SparkClient(c);
			    String loginURL = sparkAPI.getSparkHybridOpenIdURLString();
				Log.d(TAG, "loginUrl>" + loginURL);
			    view.loadUrl(loginURL);
			}
		}
		*/
		public boolean shouldOverrideUrlLoading (WebView view, String url)
		{
			Log.d(TAG, "loadUrl>" + url);
			
			Uri uri = Uri.parse(url);
			String openIdMode = null;
			String openIdSparkCode = null;
			
		    if((openIdMode = uri.getQueryParameter("openid.mode")) != null &&
		       openIdMode.equals("id_res") &&
		       (openIdSparkCode = uri.getQueryParameter("openid.spark.code")) != null)
		       {
				   Log.d(TAG, "openIdSparkCode>" + openIdSparkCode);
				   
				   new OAuth2PostTask().execute(openIdSparkCode);
				   				   
		    	   return true;
		       }
		    else
		    {
		    	
		    }

			return false;
		}
	}
	
	 private class OAuth2PostTask extends AsyncTask<String, Void, SparkSession> {
	     protected SparkSession doInBackground(String... openIdSparkCode) {
			   Map<String,String> map = new HashMap<String,String>();
			   map.put("client_id", SparkClient.sparkClientKey);
			   map.put("client_secret", SparkClient.sparkClientSecret);
			   map.put("grant_type", "authorization_code");
			   map.put("code", openIdSparkCode[0]);
			   map.put("redirect_uri", SparkClient.sparkCallbackURL);
			   
			   // TODO: move to SparkClient.authenticate()?
			   SparkSession sparkSession = null;
			   try
			   {
				   HttpPost post = new HttpPost(SparkClient.getSparkOAuth2GrantString());
				   SparkClient.initSparkHeader(post);
				   ObjectMapper mapper = new ObjectMapper();
				   StringEntity stringEntity = new StringEntity(mapper.writeValueAsString(map));
				   stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
				   post.setEntity(stringEntity);
				   HttpClient httpclient = new DefaultHttpClient(); 
				   HttpResponse response = httpclient.execute(post);
				   String responseBody = EntityUtils.toString(response.getEntity());
				   Log.d(TAG, "OAuth2 response>" + responseBody);
				   sparkSession = mapper.readValue(responseBody, SparkSession.class);
				   sparkClient.setSession(sparkSession);
				   Connection<Response> connection = sparkClient.getConnection();
				   ((ConnectionApacheHttp)connection).setHeaders(sparkClient.getHeaders());
			   } 
			   catch (Exception e)
			   {
				   Log.e(TAG, "OAuth2PostTask exception>", e);
			   }
	    	 
	    	 return sparkSession;
	     }
	     
	     protected void onPostExecute(SparkSession sparkSession) {
	 	    //Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
		 	Intent intent = new Intent(getApplicationContext(), ViewListingsActivity.class);
	 	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		    startActivity(intent);	  
		 }
	 }
}
