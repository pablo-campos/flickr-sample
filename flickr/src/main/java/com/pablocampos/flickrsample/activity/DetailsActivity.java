package com.pablocampos.flickrsample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.model.FlickrFeed;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailsActivity extends AppCompatActivity {


	public final static String FLICKR_FEED = "extra.flicker.FEED";





	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_details);

		FlickrFeed feed = (FlickrFeed) getIntent().getSerializableExtra(FLICKR_FEED);

		ImageView feedImage = findViewById(R.id.feed_image);
		Glide.with(this).load(feed.getMedia().getM()).into(feedImage);

	}

}
