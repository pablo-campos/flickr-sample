package com.pablocampos.flickrsample.adapter;

import android.view.View;

import model.FlickrFeed;

public interface FeedClickListener {

	void onClick (final View view, final FlickrFeed flickrFeed);
}
