package com.pablocampos.flickrsample.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.model.FlickrFeed;

import org.apache.commons.text.WordUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;

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
	private TextView firebaseTextRecognizer;
	private TextView firebaseLabelImage;
	private TextView feedTagsLabel;
	private ChipGroup feedTags;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Postpone enter transition until GREEN light is given
		supportPostponeEnterTransition();

		setContentView(R.layout.activity_details);

		FlickrFeed feed = (FlickrFeed) getIntent().getSerializableExtra(FLICKR_FEED);

		final ImageView feedImage = findViewById(R.id.feed_image);
		Glide.with(this)
				.asBitmap()
				.load(feed.getMedia().getM())
				.addListener(new RequestListener<Bitmap>() {
					@Override
					public boolean onLoadFailed (@Nullable final GlideException e, final Object model, final Target<Bitmap> target, final boolean isFirstResource) {
						supportStartPostponedEnterTransition();		// Proceed with enter transition
						return false;
					}



					@Override
					public boolean onResourceReady (final Bitmap bitmap, final Object model, final Target<Bitmap> target, final DataSource dataSource, final boolean isFirstResource) {
						setDetailColors(bitmap);
						setToolbarColors(bitmap);
						processFirebaseServices(bitmap);
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

		firebaseTextRecognizer  = findViewById(R.id.firebase_text_recognizer);
		firebaseLabelImage  = findViewById(R.id.firebase_label_image);

		// Update tags
		feedTagsLabel = findViewById(R.id.feed_tags_label);
		feedTagsLabel.setText(getResources().getString(R.string.tags_label));
		feedTags = findViewById(R.id.chip_group);
		feedTags.setChipSpacing(8);
		if (feed.getTags() != null && !feed.getTags().isEmpty()){
			String[] tags = feed.getTags().split(" ", 10);
			for (final String tag : tags){
				Chip chip = new Chip(feedTags.getContext());
				chip.setText(tag);
				chip.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick (final View v) {
						String escapedQuery = null;
						try {
							escapedQuery = URLEncoder.encode(tag, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
					}
				});
				feedTags.addView(chip);
			}
		} else {
			feedTagsLabel.setVisibility(View.GONE);
			feedTags.setVisibility(View.GONE);
		}
	}



	private void setDetailColors (Bitmap bitmap){

		Palette palette = createPaletteSync(bitmap);
		Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();

		int textColor = lightVibrantSwatch != null ? lightVibrantSwatch.getRgb() :  Color.WHITE;

		feedAuthor.setTextColor(textColor);
		feedDateTaken.setTextColor(textColor);
		feedDatePublished.setTextColor(textColor);
		firebaseTextRecognizer.setTextColor(textColor);
		firebaseLabelImage.setTextColor(textColor);
		feedTagsLabel.setTextColor(textColor);
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
		Palette.Swatch paletteSwatch = palette.getVibrantSwatch() != null ? palette.getVibrantSwatch() : palette.getLightVibrantSwatch();

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		int actionBarTextColor = Color.WHITE;
		int actionBarBackgroundColor = getResources().getColor(R.color.colorPrimary);
		int statusBarBackgroundColor = getResources().getColor(R.color.colorPrimaryDark);

		// Let's see if we have a palette swatch to use
		if(paletteSwatch != null){
			actionBarTextColor = paletteSwatch.getTitleTextColor();
			actionBarBackgroundColor = paletteSwatch.getRgb();
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



	/**
	 *
	 */
	public void processFirebaseServices (Bitmap bitmap) {
		FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

		// Text Recognizer
		FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
		detector.processImage(image)
						.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
							@Override
							public void onSuccess(FirebaseVisionText firebaseVisionText) {
								// Task completed successfully
								firebaseTextRecognizer.setText(String.format(getResources().getString(R.string.firebase_text_recognizer_label), firebaseVisionText.getText()));
							}
						})
						.addOnFailureListener(
								new OnFailureListener() {
									@Override
									public void onFailure(@NonNull Exception e) {
										// Task failed with an exception
										firebaseTextRecognizer.setVisibility(View.GONE);
										supportStartPostponedEnterTransition();		// Proceed with enter transition
									}
								});

		// Image Labeler
		FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler();
		labeler.processImage(image)
				.addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
					@Override
					public void onSuccess(List<FirebaseVisionImageLabel> labels) {

						// Task completed successfully
						String description = "";
						for (FirebaseVisionImageLabel label: labels) {
							String text = label.getText();
							float confidence = label.getConfidence();
							description = description.concat("\n\t" + text + " - " + confidence * 100 + "%");
						}

						firebaseLabelImage.setText(String.format(getResources().getString(R.string.firebase_label_image_label), description));
						supportStartPostponedEnterTransition();		// Proceed with enter transition
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						// Task failed with an exception
						firebaseLabelImage.setVisibility(View.GONE);
						supportStartPostponedEnterTransition();		// Proceed with enter transition
					}
				});
	}


}
