package com.pablocampos.flickrsample.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.adapter.FeedAdapter;
import com.pablocampos.flickrsample.adapter.FeedClickListener;
import com.pablocampos.flickrsample.model.ApiData;
import com.pablocampos.flickrsample.model.FlickrFeed;
import com.pablocampos.flickrsample.network.FlickrApi;
import com.pablocampos.flickrsample.network.FlickrInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrActivity extends AppCompatActivity {



	private Call<ApiData> apiCall;
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView feedGrid;
	private FeedAdapter feedAdapter;





	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_flickr);

		// Initialize grid view
		final FeedClickListener feedClickListener = new FeedClickListener() {
			@Override
			public void onClick (final View view, final FlickrFeed flickrFeed) {

				Intent intent = new Intent(FlickrActivity.this, DetailsActivity.class);
				intent.putExtra(DetailsActivity.FLICKR_FEED, flickrFeed);
				ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FlickrActivity.this, view, "feed_image");
				startActivity(intent, options.toBundle());
			}
		};

		feedAdapter = new FeedAdapter(feedClickListener);

		int numberOfColumns = 2;
		feedGrid = findViewById(R.id.feedGrid);
		feedGrid.setLayoutManager(new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL));
		feedGrid.setAdapter(feedAdapter);

		// Initialize swipe to refresh
		swipeRefreshLayout = findViewById(R.id.swiperefresh);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh () {
				performQuery("space", "space photography");
			}
		});

		performQuery("space", "space photography");
	}



	private void performQuery(final String query, final String tags){

		FlickrInterface service = FlickrApi.getClient().create(FlickrInterface.class);

		apiCall = service.getFlickrFeeds(query, tags);
		apiCall.enqueue(new Callback<ApiData>() {
			@Override
			public void onResponse(Call<ApiData> call, Response<ApiData> response) {

				// Stop refresh status
				if (swipeRefreshLayout.isRefreshing()){
					swipeRefreshLayout.setRefreshing(false);
				}

				// Update data
				feedAdapter.updateData(response.body().getItems());
			}

			@Override
			public void onFailure(Call<ApiData> call, Throwable t) {

				// Stop refresh status
				if (swipeRefreshLayout.isRefreshing()){
					swipeRefreshLayout.setRefreshing(false);
				}

				// Display an error
				Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, BaseTransientBottomBar.LENGTH_SHORT);
			}
		});
	}

}
