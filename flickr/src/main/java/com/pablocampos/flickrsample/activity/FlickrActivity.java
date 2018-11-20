package com.pablocampos.flickrsample.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.adapter.FeedAdapter;
import com.pablocampos.flickrsample.adapter.FeedClickListener;
import com.pablocampos.flickrsample.adapter.FeedItemAnimator;
import com.pablocampos.flickrsample.model.ApiData;
import com.pablocampos.flickrsample.model.FlickrFeed;
import com.pablocampos.flickrsample.network.FlickrApi;
import com.pablocampos.flickrsample.network.FlickrInterface;
import com.pablocampos.flickrsample.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrActivity extends AppCompatActivity {



	private final String CURRENT_SEARCH = "CURRENT_SEARCH";		// Current query

	private SearchView searchView;
	private Call<ApiData> apiCall;
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView feedGrid;
	private FeedAdapter feedAdapter;
	private String currentSearchQuery = "";





	@Override
	public boolean onCreateOptionsMenu( Menu menu) {

		getMenuInflater().inflate(R.menu.menu_search, menu);

		// Initialize the search functionality on the activity's toolbar
		final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) myActionMenuItem.getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {

				// Update query in case user does a swipe to refresh
				currentSearchQuery = query;

				// If search view is empty, let's update the adapter with zero items, if not, let's request a new search query:
				if (Utils.checkInternet(FlickrActivity.this)){		// Check internet connection and then perform query

					// Cancel any pending queries
					if (apiCall != null){
						apiCall.cancel();
					}

					performQuery(query);
				}

				// Update view
				if(!searchView.isIconified()) {
					searchView.setIconified(true);
				}
				myActionMenuItem.collapseActionView();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				return false;
			}
		});

		if (currentSearchQuery != null && !currentSearchQuery.isEmpty()){
			MenuItemCompat.expandActionView(myActionMenuItem);		// We need to expand the search view before
			searchView.setQuery(currentSearchQuery, false);
		}

		return true;
	}



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

		feedGrid = findViewById(R.id.feedGrid);
		feedGrid.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
		feedGrid.setItemAnimator(new FeedItemAnimator());
		feedGrid.setAdapter(feedAdapter);

		// Initialize swipe to refresh
		swipeRefreshLayout = findViewById(R.id.swiperefresh);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh () {
				performQuery("");
			}
		});

		performQuery("");
	}



	@Override
	protected void onRestoreInstanceState (final Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// Let's restore the activity's state before it was destroyed
		if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_SEARCH)){
			currentSearchQuery = savedInstanceState.getString(CURRENT_SEARCH);
		}
	}



	@Override
	protected void onSaveInstanceState (final Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current search query
		outState.putString(CURRENT_SEARCH, searchView.getQuery().toString());
	}



	private void performQuery(final String tags){

		FlickrInterface service = FlickrApi.getClient().create(FlickrInterface.class);

		apiCall = service.getFlickrFeeds(tags);
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
