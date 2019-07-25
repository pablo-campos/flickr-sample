package adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pablocampos.flickrsample.R
import model.FlickrFeed
import org.apache.commons.text.WordUtils

class FeedViewHolder internal constructor(v: View) : RecyclerView.ViewHolder(v) {


	// Each data item is just a string in this case
	private val cardView: CardView
	private val feedImage: ImageView
	private val feedTitle: TextView


	init {

		cardView = v as CardView
		cardView.setCardBackgroundColor(v.getContext().resources.getColor(R.color.cardBackgroundColor))
		feedImage = v.findViewById(R.id.feed_image)
		feedTitle = v.findViewById(R.id.feed_title)
	}


	fun initialize(feed: FlickrFeed, feedClickListener: FeedClickListener) {

		cardView.setOnClickListener { v -> feedClickListener.onClick(cardView, feed) }

		Glide.with(cardView.context)
				.asBitmap()
				.load(feed.media!!.m)
				.into(feedImage)

		// Name
		feedTitle.text = WordUtils.capitalizeFully(feed.title)
	}
}