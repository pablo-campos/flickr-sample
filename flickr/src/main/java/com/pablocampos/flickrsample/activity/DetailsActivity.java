package com.pablocampos.flickrsample.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.model.FlickrFeed;

import org.apache.commons.text.WordUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class DetailsActivity extends AppCompatActivity {


	public final static String FLICKR_FEED = "extra.flicker.FEED";

	private TextView feedAuthor;
	private TextView feedDateTaken;
	private TextView feedDatePublished;
	private TextView feedTags;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_details);

		FlickrFeed feed = (FlickrFeed) getIntent().getSerializableExtra(FLICKR_FEED);

		final ImageView feedImage = findViewById(R.id.feed_image);
		Glide.with(this)
				.asBitmap()
				.load(feed.getMedia().getM())
				.addListener(new RequestListener<Bitmap>() {
					@Override
					public boolean onLoadFailed (@Nullable final GlideException e, final Object model, final Target<Bitmap> target, final boolean isFirstResource) {
						return false;
					}



					@Override
					public boolean onResourceReady (final Bitmap resource, final Object model, final Target<Bitmap> target, final DataSource dataSource, final boolean isFirstResource) {

						updateColors(createPaletteSync(resource));
						return false;
					}
				})
		.into(feedImage);

		TextView feedTitle = findViewById(R.id.feed_title);
		feedTitle.setText(WordUtils.capitalizeFully(feed.getTitle()));

		feedAuthor = findViewById(R.id.feed_author);
		feedAuthor.setText(String.format(getResources().getString(R.string.author_label), WordUtils.capitalizeFully(feed.getAuthor())));

		feedDateTaken = findViewById(R.id.feed_date_taken);
		feedDateTaken.setText(String.format(getResources().getString(R.string.date_taken_label), feed.getDateTaken()));

		feedDatePublished = findViewById(R.id.feed_date_published);
		feedDatePublished.setText(String.format(getResources().getString(R.string.date_published_label), feed.getPublished()));

		feedTags = findViewById(R.id.feed_tags);
		feedTags.setText(String.format(getResources().getString(R.string.tags_label), feed.getTags()));
	}



	// Generate palette synchronously and return it
	private Palette createPaletteSync(Bitmap bitmap) {
		Palette p = Palette.from(bitmap).generate();
		return p;
	}



	private void updateColors (Palette palette){
		feedAuthor.setTextColor(palette.getVibrantColor(Color.WHITE));
		feedDateTaken.setTextColor(palette.getVibrantColor(Color.WHITE));
		feedDatePublished.setTextColor(palette.getVibrantColor(Color.WHITE));
		feedTags.setTextColor(palette.getVibrantColor(Color.WHITE));

		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getLightMutedColor(Color.WHITE)));

		Window window = getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(palette.getMutedColor(Color.WHITE));
	}

}
