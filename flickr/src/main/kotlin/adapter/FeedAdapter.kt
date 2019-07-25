package adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pablocampos.flickrsample.R
import model.FlickrFeed
import java.util.*

class FeedAdapter// data is passed into the constructor
(private val feedClickListener: FeedClickListener) : RecyclerView.Adapter<FeedViewHolder>() {


	private val feeds: MutableList<FlickrFeed>


	init {
		this.feeds = ArrayList()
	}


	// inflates the cell layout from xml when needed
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.feed_grid_item, parent, false)
		return FeedViewHolder(view)
	}


	// binds the data to the TextView in each cell
	override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
		holder.initialize(feeds[position], feedClickListener)
	}


	// total number of cells
	override fun getItemCount(): Int {
		return feeds.size
	}


	fun updateData(newData: List<FlickrFeed>) {
		feeds.clear()
		feeds.addAll(newData)
		notifyDataSetChanged()
	}
}