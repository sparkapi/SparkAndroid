package com.sparkplatform.ui;

import java.io.InputStream;
import java.net.URL;
//
//Copyright (c) 2013 Financial Business Systems, Inc. All rights reserved.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//

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
