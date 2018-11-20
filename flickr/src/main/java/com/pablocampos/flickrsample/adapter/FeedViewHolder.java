package com.pablocampos.flickrsample.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.model.FlickrFeed;

import org.apache.commons.text.WordUtils;

public class FeedViewHolder extends RecyclerView.ViewHolder {




	// Each data item is just a string in this case
	private CardView cardView;
	private ImageView feedImage;
	private TextView feedTitle;




	FeedViewHolder(View v) {
		super(v);

		cardView = (CardView) v;
		cardView.setCardBackgroundColor(v.getContext().getResources().getColor(R.color.cardBackgroundColor));
		feedImage = v.findViewById(R.id.feed_image);
		feedTitle = v.findViewById(R.id.feed_title);
	}



	public void initialize(final FlickrFeed feed, final FeedClickListener feedClickListener){

		cardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (final View v) {
				feedClickListener.onClick(cardView, feed);
			}
		});

		Glide.with(cardView.getContext())
				.asBitmap()
				.load(feed.getMedia().getM())
				.into(feedImage);

		// Name
		feedTitle.setText(WordUtils.capitalizeFully(feed.getTitle()));
	}
}