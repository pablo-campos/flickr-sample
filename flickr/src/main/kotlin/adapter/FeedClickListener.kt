package adapter

import android.view.View

import model.FlickrFeed

interface FeedClickListener {

	fun onClick(view: View, flickrFeed: FlickrFeed)
}
