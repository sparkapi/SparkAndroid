package com.sparkplatform.ui;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
	 
	// class vars *************************************************************
	
	private static final String TAG = "ImageLoaderTask";
	
	// instance vars **********************************************************
	
	 private ImageView imageView;
	 
	// interface **************************************************************
	
	 public void setImageView(ImageView imageView)
	 {
		 this.imageView = imageView;
	 }
	 
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
   		 Log.e(TAG, "exception", e);
   		 return null;
   	 }
    }
    
    protected void onPostExecute(Bitmap bitmap) {
   	 if(bitmap != null)
   		 imageView.setImageBitmap(bitmap);
    }
}
