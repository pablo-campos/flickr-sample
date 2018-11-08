package com.pablocampos.flickrsample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.model.FlickrFeed;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedViewHolder> {


	private List<FlickrFeed> feeds;
	private FeedClickListener feedClickListener;




	// data is passed into the constructor
	public FeedAdapter(final FeedClickListener feedClickListener) {
		this.feeds = new ArrayList<>();
		this.feedClickListener = feedClickListener;
	}



	// inflates the cell layout from xml when needed
	@Override
	@NonNull
	public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_grid_item, parent, false);
		return new FeedViewHolder(view);
	}



	// binds the data to the TextView in each cell
	@Override
	public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
		holder.initialize(feeds.get(position), feedClickListener);
	}



	// total number of cells
	@Override
	public int getItemCount() {
		return feeds.size();
	}



	public void updateData(final List<FlickrFeed> newData){
		feeds.clear();
		feeds.addAll(newData);
		notifyDataSetChanged();
	}
}