package com.sparkplatform.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class SlideshowActivity extends Activity {

	// instance vars **********************************************************
	
	private List<String> photos;

	// interface **************************************************************
	
	@SuppressWarnings({ "unchecked", "deprecation" })
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
        private static final int ITEM_WIDTH = 136;
        private static final int ITEM_HEIGHT = 88;

        //private final int mGalleryItemBackground;
        private final Context mContext;

        private final float mDensity;

        public ImageAdapter(Context c) {
            mContext = c;
            // See res/values/attrs.xml for the <declare-styleable> that defines
            // Gallery1.
            //TypedArray a = obtainStyledAttributes(R.styleable.Gallery1);
            //mGalleryItemBackground = a.getResourceId(
            //        R.styleable.Gallery1_android_galleryItemBackground, 0);
            //a.recycle();

            mDensity = c.getResources().getDisplayMetrics().density;
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
                convertView = new ImageView(mContext);

                imageView = (ImageView) convertView;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new Gallery.LayoutParams(
                        (int) (ITEM_WIDTH * mDensity + 0.5f),
                        (int) (ITEM_HEIGHT * mDensity + 0.5f)));
            
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
