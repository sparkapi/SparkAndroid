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

import com.sparkplatform.api.SparkClient;
import com.sparkplatform.api.SparkSession;
import com.sparkplatform.ui.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences p = getSharedPreferences(UIConstants.SPARK_PREFERENCES, MODE_PRIVATE);
		String accessToken = p.getString(UIConstants.AUTH_ACCESS_TOKEN, null);
		String refreshToken = p.getString(UIConstants.AUTH_REFRESH_TOKEN, null);
		String openIdToken = p.getString(UIConstants.AUTH_OPENID, null);
		Intent intent = null;
		if(accessToken != null && refreshToken != null)
		{
			SparkSession session = new SparkSession();
			session.setAccessToken(accessToken);
			session.setRefreshToken(refreshToken);
			SparkClient.getInstance().setSession(session);
			intent = new Intent(this, ViewListingsActivity.class);
		}
		else if(openIdToken != null)
		{
			SparkSession session = new SparkSession();
			session.setOpenIdToken(openIdToken);
			SparkClient.getInstance().setSession(session);
			intent = new Intent(this, MyAccountActivity.class);
		}
		else
			intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
