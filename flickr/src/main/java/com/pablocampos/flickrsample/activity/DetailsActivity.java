package com.pablocampos.flickrsample.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
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

import static com.pablocampos.flickrsample.utils.Utils.createPaletteSync;
import static com.pablocampos.flickrsample.utils.Utils.manipulateColor;

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
					public boolean onResourceReady (final Bitmap bitmap, final Object model, final Target<Bitmap> target, final DataSource dataSource, final boolean isFirstResource) {

						setDetailColors(bitmap);
						setToolbarColors(bitmap);
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



	private void setDetailColors (Bitmap bitmap){

		Palette palette = createPaletteSync(bitmap);
		Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();

		int textColor = lightVibrantSwatch != null ? lightVibrantSwatch.getRgb() :  Color.WHITE;

		feedAuthor.setTextColor(textColor);
		feedDateTaken.setTextColor(textColor);
		feedDatePublished.setTextColor(textColor);
		feedTags.setTextColor(textColor);
	}



	/**
	 * Set the background and text colors of a toolbar given a
	 * bitmap image to match
	 * @param bitmap image
	 */
	public void setToolbarColors (Bitmap bitmap) {
		// Generate the palette and get the vibrant swatch
		// See the createPaletteSync() method
		// from the code snippet above
		Palette palette = createPaletteSync(bitmap);
		Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();		// palette.getDarkVibrantSwatch()

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		int actionBarTextColor = Color.WHITE;
		int actionBarBackgroundColor = getResources().getColor(R.color.colorPrimary);
		int statusBarBackgroundColor = getResources().getColor(R.color.colorPrimaryDark);

		// Check that the Vibrant swatch is available and set the toolbar background and text colors
		if(vibrantSwatch != null){
			actionBarTextColor = vibrantSwatch.getTitleTextColor();
			actionBarBackgroundColor = vibrantSwatch.getRgb();
			statusBarBackgroundColor = manipulateColor(actionBarBackgroundColor, 0.8f);
		}

		// Update toolbar colors:
		toolbar.setTitleTextColor(actionBarTextColor);
		toolbar.setBackgroundColor(actionBarBackgroundColor);

		// Update status nar color:
		Window window = getWindow();
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		window.setStatusBarColor(statusBarBackgroundColor);
	}


}
