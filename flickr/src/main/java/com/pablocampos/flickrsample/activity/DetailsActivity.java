package com.pablocampos.flickrsample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.model.FlickrFeed;

import org.apache.commons.text.WordUtils;

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

		TextView feedTitle = findViewById(R.id.feed_title);
		feedTitle.setText(WordUtils.capitalizeFully(feed.getTitle()));

		TextView feedAuthor = findViewById(R.id.feed_author);
		feedAuthor.setText(String.format(getResources().getString(R.string.author_label), WordUtils.capitalizeFully(feed.getAuthor())));

		TextView feedDateTaken = findViewById(R.id.feed_date_taken);
		feedDateTaken.setText(String.format(getResources().getString(R.string.date_taken_label), feed.getDateTaken()));

		TextView feedDatePublished = findViewById(R.id.feed_date_published);
		feedDatePublished.setText(String.format(getResources().getString(R.string.date_published_label), feed.getPublished()));

		TextView feedTags = findViewById(R.id.feed_tags);
		feedTags.setText(String.format(getResources().getString(R.string.tags_label), feed.getTags()));
	}

}
