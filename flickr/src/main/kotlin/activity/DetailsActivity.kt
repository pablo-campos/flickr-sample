package activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.pablocampos.flickrsample.R
import model.FlickrFeed
import org.apache.commons.text.WordUtils
import utils.Utils
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class DetailsActivity : AppCompatActivity() {

	private lateinit var feedAuthor: TextView
	private lateinit var feedDateTaken: TextView
	private lateinit var feedDatePublished: TextView
	private lateinit var firebaseTextRecognizer: TextView
	private lateinit var firebaseTextRecognizerValue: TextView
	private lateinit var firebaseLabelImage: TextView
	private lateinit var firebaseLabelImageValue: TextView
	private lateinit var feedTagsLabel: TextView



    companion object {
        const val FLICKR_FEED = "extra.flicker.FEED"
    }


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Postpone enter transition until GREEN light is given
		supportPostponeEnterTransition()

		setContentView(R.layout.activity_details)

		val feed = intent.getSerializableExtra(FLICKR_FEED) as FlickrFeed

		val feedImage = findViewById<ImageView>(R.id.feed_image)
		Glide.with(this)
				.asBitmap()
				.load(feed.media!!.m)
				.addListener(object : RequestListener<Bitmap> {
					override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
						supportStartPostponedEnterTransition()        // Proceed with enter transition
						return false
					}


					override fun onResourceReady(bitmap: Bitmap, model: Any, target: Target<Bitmap>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
						setToolbarColors(bitmap)
						processFirebaseTextRecognizer(bitmap)
						return false
					}
				})
				.into(feedImage)

		val feedTitle = findViewById<TextView>(R.id.feed_title)
		feedTitle.text = WordUtils.capitalizeFully(feed.title)

		feedAuthor = findViewById(R.id.feed_author)
		feedAuthor.text = String.format(resources.getString(R.string.author_label), WordUtils.capitalizeFully(feed.author))

		feedDateTaken = findViewById(R.id.feed_date_taken)
		feedDateTaken.text = String.format(resources.getString(R.string.date_taken_label), feed.dateTaken)

		feedDatePublished = findViewById(R.id.feed_date_published)
		feedDatePublished.text = String.format(resources.getString(R.string.date_published_label), feed.published)

		firebaseTextRecognizer = findViewById(R.id.firebase_text_recognizer)
		firebaseTextRecognizer.text = resources.getString(R.string.firebase_text_recognizer_label)
		firebaseTextRecognizerValue = findViewById(R.id.firebase_text_recognizer_value)
		firebaseLabelImage = findViewById(R.id.firebase_label_image)
		firebaseLabelImage.text = resources.getString(R.string.firebase_label_image_label)
		firebaseLabelImageValue = findViewById(R.id.firebase_label_image_value)

		// Update tags
		feedTagsLabel = findViewById(R.id.feed_tags_label)
		feedTagsLabel.text = resources.getString(R.string.tags_label)
		val feedTags = findViewById<ChipGroup>(R.id.chip_group)
		feedTags.setChipSpacing(8)
		if (feed.tags != null && feed.tags!!.isNotEmpty()) {
			val tags = feed.tags!!.split(" ".toRegex(), 10).toTypedArray()
			for (tag in tags) {
				val chip = Chip(feedTags.context)
				chip.text = tag
				chip.setOnClickListener {
					var escapedQuery: String? = null
					try {
						escapedQuery = URLEncoder.encode(tag, "UTF-8")
					} catch (e: UnsupportedEncodingException) {
						e.printStackTrace()
					}

					val uri = Uri.parse("http://www.google.com/search?q=" + escapedQuery!!)
					val intent = Intent(Intent.ACTION_VIEW, uri)
					startActivity(intent)
				}
				feedTags.addView(chip)
			}
		} else {
			feedTagsLabel.visibility = View.GONE
			feedTags.visibility = View.GONE
		}


		setDetailColors()
	}


	private fun setDetailColors() {
		feedAuthor.setTextColor(Color.WHITE)
		feedDateTaken.setTextColor(Color.LTGRAY)
		feedDatePublished.setTextColor(Color.LTGRAY)
		firebaseTextRecognizer.setTextColor(Color.WHITE)
		firebaseTextRecognizerValue.setTextColor(Color.LTGRAY)
		firebaseLabelImage.setTextColor(Color.WHITE)
		firebaseLabelImageValue.setTextColor(Color.LTGRAY)
		feedTagsLabel.setTextColor(Color.WHITE)
	}


	/**
	 * Set the background and text colors of a toolbar given a
	 * bitmap image to match
	 * @param bitmap image
	 */
	fun setToolbarColors(bitmap: Bitmap) {
		// Generate the palette and get the vibrant swatch
		// See the createPaletteSync() method
		// from the code snippet above
		val palette = Utils.createPaletteSync(bitmap)
		val paletteSwatch = if (palette.vibrantSwatch != null) palette.vibrantSwatch else palette.lightVibrantSwatch

		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		var actionBarTextColor = Color.WHITE
		var actionBarBackgroundColor = ContextCompat.getColor(this, R.color.colorPrimary)
		var statusBarBackgroundColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)

		// Let's see if we have a palette swatch to use
		if (paletteSwatch != null) {
			actionBarTextColor = paletteSwatch.titleTextColor
			actionBarBackgroundColor = paletteSwatch.rgb
			statusBarBackgroundColor = Utils.manipulateColor(actionBarBackgroundColor, 0.8f)
		}

		// Update toolbar colors:
		toolbar.setTitleTextColor(actionBarTextColor)
		toolbar.setBackgroundColor(actionBarBackgroundColor)

		// Update status nar color:
		val window = window
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
		window.statusBarColor = statusBarBackgroundColor
	}


	/**
	 * ML Kit APIs, we apply "Text Recognizer" based on the Bitmap downloaded via Glide.
	 */
	fun processFirebaseTextRecognizer(bitmap: Bitmap) {

        // Create json request to cloud vision
        val request = JsonObject()

        // Add image to request
        val image = JsonObject()
        image.add("content", JsonPrimitive(Utils.base64Encoder(Utils.scaleBitmapDown(bitmap, 640))))
        request.add("image", image)

        // Add features to the request
        val feature = JsonObject()
        feature.add("type", JsonPrimitive("TEXT_DETECTION"))

        val features = JsonArray()
        features.add(feature)
        request.add("features", features)

        Utils.annotateImage(request.toString())
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {

                    // Task failed with an exception
                    firebaseTextRecognizerValue.text = resources.getString(R.string.firebase_server_error)
                    processFirebaseImageLabeler(bitmap)        // Proceed with image label detection

                } else {

                    // Task completed successfully
                    val annotation = task.result!!.asJsonArray[0].asJsonObject["fullTextAnnotation"].asJsonObject
                    for (page in annotation["pages"].asJsonArray) {
                        var pageText = ""
                        for (block in page.asJsonObject["blocks"].asJsonArray) {
                            var blockText = ""
                            for (para in block.asJsonObject["paragraphs"].asJsonArray) {
                                var paraText = ""
                                for (word in para.asJsonObject["words"].asJsonArray) {
                                    var wordText = ""
                                    for (symbol in word.asJsonObject["symbols"].asJsonArray) {
                                        wordText += symbol.asJsonObject["text"].asString
                                        System.out.format("Symbol text: %s (confidence: %f)%n",
                                            symbol.asJsonObject["text"].asString, symbol.asJsonObject["confidence"].asFloat)
                                    }
                                    System.out.format("Word text: %s (confidence: %f)%n%n", wordText,
                                        word.asJsonObject["confidence"].asFloat)
                                    System.out.format("Word bounding box: %s%n", word.asJsonObject["boundingBox"])
                                    paraText = String.format("%s%s ", paraText, wordText)
                                }
                                System.out.format("%nParagraph: %n%s%n", paraText)
                                System.out.format("Paragraph bounding box: %s%n", para.asJsonObject["boundingBox"])
                                System.out.format("Paragraph Confidence: %f%n", para.asJsonObject["confidence"].asFloat)
                                blockText += paraText
                            }
                            pageText += blockText
                        }
                    }

                    firebaseTextRecognizerValue.text = "Add something here..."
                    processFirebaseImageLabeler(bitmap)     // Proceed with image label detection
                }
            }
	}


	/**
	 * ML Kit APIs, we apply "Image Labeler" based on the Bitmap downloaded via Glide.
	 */
    private fun processFirebaseImageLabeler(bitmap: Bitmap) {

        // Create json request to cloud vision
        val request = JsonObject()

        // Add image to request
        val image = JsonObject()
        image.add("content", JsonPrimitive(Utils.base64Encoder(Utils.scaleBitmapDown(bitmap, 640))))
        request.add("image", image)

        // Add features to the request
        val feature = JsonObject()
        feature.add("maxResults", JsonPrimitive(5))
        feature.add("type", JsonPrimitive("LABEL_DETECTION"))

        val features = JsonArray()
        features.add(feature)
        request.add("features", features)

        Utils.annotateImage(request.toString())
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {

                    // Task failed with an exception
                    firebaseLabelImageValue.text = resources.getString(R.string.firebase_server_error)
                    supportStartPostponedEnterTransition()        // Proceed with enter transition

                } else {

                    // Task completed successfully
                    var description = ""
                    for (label in task.result!!.asJsonArray[0].asJsonObject["labelAnnotations"].asJsonArray) {
                        val labelObj = label.asJsonObject
                        val text = labelObj["description"]
                        val entityId = labelObj["mid"]
                        val confidence = labelObj["score"]

                        description += if (description.isEmpty()) {
                            (text.toString() + " - " + confidence.asInt * 100 + "%")
                        } else {
                            ("\n" + text + " - " + confidence.asInt * 100 + "%")
                        }
                    }

                    firebaseLabelImageValue.text = description
                    supportStartPostponedEnterTransition()        // Proceed with enter transition
                }
            }
	}


}
