package com.sparkplatform.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;

public class LoginActivity extends Activity {
	
	private static final String TAG = "LoginActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	public void sparkLogin(View view) {
		Log.d(TAG, "sparkLogin");
	
	    Intent intent = new Intent(this, WebViewActivity.class);
	    Switch s = (Switch) findViewById(R.id.switch1);
	    intent.putExtra(UIConstants.EXTRA_LOGIN_HYBRID, s.isChecked());
	    startActivity(intent);
	}
	


}
