Spark Android API and Example App
=================================

The `SparkAPI` object is designed as a standalone Java interface for use with the [Spark API](http://www.sparkplatform.com/docs/overview/api).  It implements Spark [authentication](http://www.sparkplatform.com/docs/authentication/authentication) via the Hybrid or OpenID methods.  API calls per HTTP method provide a high-level Spark API interface and return a JSON results array on success while handling errors like session expiration for the client.

This project includes an example Android app that makes use of `SparkAPI` object to authenticate via Hybrid or OpenID methods, search listings, view listings, view an individual listing with photos and standard fields, and view a user account.  View app [screenshots](./Spark Android Screenshots.pdf) on a Nexus 7.

## Requirements

* Android 4.0 or later (API level 14 / Ice Cream Sandwich)
* Eclipse IDE with Android SDK Tools [installed](http://developer.android.com/sdk/installing/index.html).

## Configuration

Once you [register](http://www.sparkplatform.com/register/developers) as a Spark developer and receive your Spark Client Id and Client Secret, open the [SparkAPI.java](./src/com/sparkplatform/api/SparkAPI.java) file and set the `sparkClientKey` and `sparkClientSecret` class variables.  You must also set the `sparkAPIUserAgent` with the name of your app or your API requests will not be accepted.  The `sparkCallbackURL` can also be customized but you most likely will want to use the default value to start.

Get a handle to a `SparkAPI` object via the `getInstance()` singleton static method.  If the instance has not been created, `SparkAPI` will be instantiated and returned with your configuration.

``` java
public class SparkAPI extends Client {

	// configuration **********************************************************
	
	public static final String sparkClientKey = "<YOUR OAUTH2 CLIENT KEY>";
	public static final String sparkClientSecret = "<YOUR OAUTH2 CLIENT SECRET>";
	public static final String sparkAPIUserAgent = null;
	public static final String sparkCallbackURL = "https://sparkplatform.com/oauth2/callback";
```

## API Examples

### Authentication

The `SparkAPI` object is designed to work with Android `WebView` and `WebViewClient` objects to initiate and process Spark authentication.

**Initiating an Authentication Request**:

* To initiate a Hybrid authentication request, call `WebView loadUrl()` with `getSparkHybridOpenIdURLString`.

* To initiate an OpenID authentication request, call `WebView loadUrl()` with `getSparkOpenIdURLString` or `getSparkOpenIdAttributeExchangeURLString`.

**Processing Authentication**:

`SparkAPI` provides methods for processing authentication and setting a `SparkSession` object upon success: 

* **isHybridAuthorized** and **hybridAuthenticate** implement the Spark [OpenID+OAuth 2 Hybrid Protocol](http://www.sparkplatform.com/docs/authentication/openid_oauth2_authentication).
* **openIdAuthenticate** implements the Spark OpenID [Simple Registration Extension](http://www.sparkplatform.com/docs/authentication/openid_authentication#sreg) or [OpenID Attribute Exchange Extension](http://www.sparkplatform.com/docs/authentication/openid_authentication#ax).

``` java
public static String isHybridAuthorized(String url);

public SparkSession hybridAuthenticate(String openIdSparkCode) throws SparkAPIClientException;

public SparkSession openIdAuthenticate(String url) throws SparkAPIClientException;
```

These authentication methods are typically placed in a `WebViewClient` object to respond to a URL request generated after the user provides their Spark credentials.  See [WebViewActivity.java](./src/com/sparkplatform/ui/WebViewActivity.java) for an example.


``` java
		public boolean shouldOverrideUrlLoading (WebView view, String url)
		{
		    String openIdSparkCode = null;
		    if(loginHybrid && (openIdSparkCode = SparkAPI.isHybridAuthorized(url)) != null)
		    {
				Log.d(TAG, "openIdSparkCode>" + openIdSparkCode);
				new OAuth2PostTask().execute(openIdSparkCode);	   				   
				return true;
		    }

		    return false;
		}
		
	private class OAuth2PostTask extends AsyncTask<String, Void, SparkSession> {
	     protected SparkSession doInBackground(String... openIdSparkCode) {
	    	 SparkSession session = null;
	    	 try
	    	 {
	    		 session = sparkClient.hybridAuthenticate(openIdSparkCode[0]);
	    	 }
	    	 catch(SparkAPIClientException e)
	    	 {
	    		 Log.e(TAG, "SparkApiClientException", e);
	    	 }
	    	 
	    	 return session;
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
```

### Making API calls

Once an authenticated `SparkSession` is set, `SparkAPI` methods corresponding to the four HTTP methods can be called.  

On success, a Spark `Response` object is returned with convenience methods to access the JSON, return code, and results array.

On failure, a `SparkAPIClientException` is thrown.

Session renewal is handled automatically by the `SparkAPI` object when a session token expire [error code](http://www.sparkplatform.com/docs/supporting_documentation/error_codes) is returned by the API.

``` java
public Response get(String path, Map<ApiParameter, String> options) throws SparkAPIClientException;

public Response post(String path, String body, Map<ApiParameter, String> options) throws SparkAPIClientException;

public Response put(String path, String body, Map<ApiParameter, String> options) throws SparkAPIClientException;

public Response delete(String path, Map<ApiParameter, String> options) throws SparkAPIClientException;
```

Below is an example API call to the `/my/account` Spark API endpoint from the example app.  On response, the list view interface is updated.

``` java
	 private class MyAccountTask extends AsyncTask<Void, Void, Response> {
	     protected Response doInBackground(Void... v) {
				   
	    	 Response r = null;
	    	 try
	    	 {
	    		 r = SparkAPI.getInstance().get("/my/account",null);
	    	 }
	    	 catch(SparkAPIClientException e)
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
	    				 R.layout.two_line_list_item, 
	    				 new String[] {"line1", "line2"}, 
	    				 new int[] {android.R.id.text1, android.R.id.text2});
	    		 setListAdapter(adapter);
	    	 }
	     }
	 }
```

### Getting Started with your own App

The example app provides a great starting point for building your own Spark-powered Android app.  At a minimum, the core authentication features encapsulated by `LoginActivity` and `WebViewActivity` can be repurposed.

In your `MainActivity` `onCreate` method, you will need code similar to below that reads any saved tokens and bypasses Login if the session is valid to show your home `Activity`.  If the session is not valid, the `LoginActivity` is presented.

``` java
public class MainActivity extends Activity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		
		startActivity(intent);
	}
```

In `WebViewActivity`, the `processAuthentication` method should also be modified to save any session state (securely to SharedPreferences or other storage) as well as redirect the user to the top `Activity`.

``` java
	 private class OAuth2PostTask extends AsyncTask<String, Void, SparkSession> {

		...
	     
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
		SharedPreferences p = getSharedPreferences(UIConstants.SPARK_PREFERENCES, MODE_PRIVATE);
		SharedPreferences.Editor editor = p.edit();
		editor.putString(UIConstants.AUTH_ACCESS_TOKEN, session.getAccessToken());
		editor.putString(UIConstants.AUTH_REFRESH_TOKEN, session.getRefreshToken());
		editor.commit(); 
	 }
```

## Dependencies

* [Apache commons-codec](http://commons.apache.org/codec/)
* [Apache commons-lang3](http://commons.apache.org/lang/)
* [Apache commons-logging](http://commons.apache.org/logging/)
* [Jackson JSON processor](http://jackson.codehaus.org/)
* [JodaTime](http://joda-time.sourceforge.net/)
* [log4j](http://logging.apache.org/log4j/1.2/)

## Compatibility

Tested OSs: Android 4.2 Jelly Bean (Version 17), Android 4.0 Ice Cream Sandwich (Version 14)

Tested Eclipse versions: 3.7 Indigo

Tested Devices: Nexus 7, Nexus 4
