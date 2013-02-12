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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
    private boolean loginHybrid;
    
    // interface **************************************************************
    
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
	    this.sparkClient = SparkClient.getInstance();
	    
		Intent intent = getIntent();
		loginHybrid = intent.getBooleanExtra(UIConstants.EXTRA_LOGIN_HYBRID, true);
		String loginURL = loginHybrid ? 
				sparkClient.getSparkHybridOpenIdURLString() : 
				sparkClient.getSparkOpenIdAttributeExchangeURLString();
		
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
			
			String openIdSparkCode = null;
		    if(loginHybrid && (openIdSparkCode = SparkClient.isHybridAuthorized(url)) != null)
		    {
				   Log.d(TAG, "openIdSparkCode>" + openIdSparkCode);
				   new OAuth2PostTask().execute(openIdSparkCode);	   				   
		    	   return true;
		    }
		    else if(!loginHybrid && sparkClient.openIdAuthenticate(url) != null)
		    {
	    		processAuthentication((SparkSession)sparkClient.getSession(), url);
		    	
	    		Intent intent = new Intent(getApplicationContext(), MyAccountActivity.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(intent);
	    		return true;
		    }

			return false;
		}
	}
	
	 private class OAuth2PostTask extends AsyncTask<String, Void, SparkSession> {
	     protected SparkSession doInBackground(String... openIdSparkCode) {
	    	 return sparkClient.hybridAuthenticate(openIdSparkCode[0]);
	     }
	     
	     protected void onPostExecute(SparkSession sparkSession) {	    	 
	    	if(sparkSession != null)
	    	{
	    		processAuthentication(sparkSession, null);
	    		
	    		Intent intent = new Intent(getApplicationContext(), ViewListingsActivity.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		startActivity(intent);	  
	    	}
		 }
	 }
	 
	 private void processAuthentication(SparkSession session, String url)
	 {
		 if(session.getOpenIdToken() != null)
		 {
				List<NameValuePair> params = SparkClient.getURLParams(url);
				String value = SparkClient.getParameter(params, "openid.ax.value.id");
				if(value != null)
					System.setProperty(UIConstants.PROPERTY_OPENID_ID, value);
				value = SparkClient.getParameter(params, "openid.ax.value.friendly");
				if(value != null)
					System.setProperty(UIConstants.PROPERTY_OPENID_FRIENDLY, value);
				value = SparkClient.getParameter(params, "openid.ax.value.first_name");
				if(value != null)
					System.setProperty(UIConstants.PROPERTY_OPENID_FIRST_NAME, value);
				value = SparkClient.getParameter(params, "openid.ax.value.middle_name");
				if(value != null)
					System.setProperty(UIConstants.PROPERTY_OPENID_MIDDLE_NAME, value);
				value = SparkClient.getParameter(params, "openid.ax.value.last_name");
				if(value != null)
					System.setProperty(UIConstants.PROPERTY_OPENID_LAST_NAME, value);
				value = SparkClient.getParameter(params, "openid.ax.value.email");
				if(value != null)
					System.setProperty(UIConstants.PROPERTY_OPENID_EMAIL, value);
			 
		 }
		 else
		 {
		 }
	 }
}
