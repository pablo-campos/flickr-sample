package com.pablocampos.flickrsample;

import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

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
	private RecyclerView feedGrid;
	private FeedAdapter feedAdapter;





	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flickr);

		// Initialize grid view
		final FeedClickListener feedClickListener = new FeedClickListener() {
			@Override
			public void onClick (final FlickrFeed flickrFeed) {
				// Do something here
			}
		};

		feedAdapter = new FeedAdapter(feedClickListener);

		int numberOfColumns = 2;
		feedGrid = findViewById(R.id.feedGrid);
		feedGrid.setLayoutManager(new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL));
		feedGrid.setAdapter(feedAdapter);

		performQuery("german shepherd", "dog");
	}



	private void performQuery(final String query, final String tags){

		FlickrInterface service = FlickrApi.getClient().create(FlickrInterface.class);

		apiCall = service.getFlickrFeeds(query, tags);
		apiCall.enqueue(new Callback<ApiData>() {
			@Override
			public void onResponse(Call<ApiData> call, Response<ApiData> response) {
				feedAdapter.updateData(response.body().getItems());
			}

			@Override
			public void onFailure(Call<ApiData> call, Throwable t) {

				// Display an error
				Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, BaseTransientBottomBar.LENGTH_SHORT);
			}
		});
	}

}
