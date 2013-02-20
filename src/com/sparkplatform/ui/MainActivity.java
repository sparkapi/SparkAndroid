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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.sparkplatform.api.SparkAPI;
import com.sparkplatform.api.SparkAPIClientException;
import com.sparkplatform.api.SparkSession;
import com.sparkplatform.api.core.Configuration;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
		configureLog4j();
		configureSparkAPI();
		
		Intent intent = getMainIntent();
		startActivity(intent);
	}
	
	private void configureLog4j()
	{
        LogConfigurator logConfigurator = new LogConfigurator();        
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("com.sparkplatform", Level.INFO);
        logConfigurator.setUseFileAppender(false);
        logConfigurator.configure();
	}
	
	private void configureSparkAPI()
	{
	    try
	    {
			Configuration c = new Configuration();
		    Resources resources = getResources();
		    InputStream rawResource = resources.openRawResource(R.raw.sparkapi);
		    Properties properties = new Properties();
	    	properties.load(rawResource);
			Configuration.loadFromProperties(c, properties);
			SparkAPI.setConfiguration(c);
	    }
	    catch(IOException e)
	    {
	    	Log.e(TAG, "loadConfiguration", e);
	    }
	}
	
	private Intent getMainIntent()
	{
		Intent intent = null;
		
		try
		{
			SharedPreferences p = getSharedPreferences(UIConstants.SPARK_PREFERENCES, MODE_PRIVATE);
			String accessToken = p.getString(UIConstants.AUTH_ACCESS_TOKEN, null);
			String refreshToken = p.getString(UIConstants.AUTH_REFRESH_TOKEN, null);
			String openIdToken = p.getString(UIConstants.AUTH_OPENID, null);
			if(accessToken != null && refreshToken != null)
			{
				SparkSession session = new SparkSession();
				session.setAccessToken(accessToken);
				session.setRefreshToken(refreshToken);
				SparkAPI.getInstance().setSession(session);
				intent = new Intent(this, ViewListingsActivity.class);
			}
			else if(openIdToken != null)
			{
				SparkSession session = new SparkSession();
				session.setOpenIdToken(openIdToken);
				SparkAPI.getInstance().setSession(session);
				intent = new Intent(this, MyAccountActivity.class);
			}
			else
				intent = new Intent(this, LoginActivity.class);
		}
		catch(SparkAPIClientException e)
		{
			Log.e(TAG, "SparkApiClientException", e);
		}
		
		return intent;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
