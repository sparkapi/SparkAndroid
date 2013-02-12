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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class SlideshowActivity extends Activity {

	// instance vars **********************************************************
	
	private List<String> photos;

	// interface **************************************************************
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slideshow);
		
		Intent i = getIntent();
		photos = (List<String>)i.getSerializableExtra(UIConstants.EXTRA_PHOTOS);		
		
        // Reference the Gallery view
        Gallery g = (Gallery) findViewById(R.id.gallery);
        // Set the adapter to our custom adapter (below)
        g.setAdapter(new ImageAdapter(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_slideshow, menu);
		return true;
	}
	
    public class ImageAdapter extends BaseAdapter {

        //private final int mGalleryItemBackground;
        private final Context context;

        private final int width;
        private final int height;
        private final float density;

        public ImageAdapter(Context c) {
            context = c;
            // See res/values/attrs.xml for the <declare-styleable> that defines
            // Gallery1.
            //TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
            //mGalleryItemBackground = a.getResourceId(
            //        R.styleable.Gallery1_android_galleryItemBackground, 0);
            //a.recycle();

            density = c.getResources().getDisplayMetrics().density;
            
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }

        public int getCount() {
            return photos != null ? photos.size() : 0;
        }

        public Object getItem(int position) {
            return photos != null ? photos.get(position) : null;
        }

        public long getItemId(int position) {
            return position;
        }

		public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = new ImageView(context);

                imageView = (ImageView) convertView;
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setLayoutParams(new Gallery.LayoutParams(
                        (int) (width * density),
                        (int) (height * density)));
            
                // The preferred Gallery item background
                //imageView.setBackgroundResource(mGalleryItemBackground);
            } else {
                imageView = (ImageView) convertView;
            }

            String uri = (String)getItem(position);
            if(uri != null)
            {
   			 	ImageLoaderTask task = new ImageLoaderTask();
   			 	task.setImageView(imageView);
   			 	task.execute(uri);
            }

            return imageView;
        }
    }

}
