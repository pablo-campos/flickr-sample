package com.pablocampos.flickrsample.activity;

import android.app.ActivityOptions;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
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
import com.pablocampos.flickrsample.model.ApiDataViewModel;
import com.pablocampos.flickrsample.model.DataWrapper;
import com.pablocampos.flickrsample.model.FlickrFeed;
import com.pablocampos.flickrsample.utils.Utils;

public class FlickrActivity extends AppCompatActivity {


	// LiveData ViewModel
	private ApiDataViewModel apiDataViewModel;

	// Adapters and views
	private SearchView searchView;
	private SwipeRefreshLayout swipeRefreshLayout;
	private RecyclerView recyclerView;
	private FeedAdapter feedAdapter;





	@Override
	public boolean onCreateOptionsMenu( Menu menu) {

		getMenuInflater().inflate(R.menu.menu_search, menu);

		// Initialize the search functionality on the activity's toolbar
		final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) myActionMenuItem.getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {

				// If search view is empty, let's update the adapter with zero items, if not, let's request a new search query:
				if (Utils.checkInternet(FlickrActivity.this)){		// Check internet connection and then perform query
					apiDataViewModel.loadFeeds(query);
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

		recyclerView = findViewById(R.id.feedGrid);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
		recyclerView.setItemAnimator(new FeedItemAnimator());
		recyclerView.setAdapter(feedAdapter);

		// Initialize swipe to refresh
		swipeRefreshLayout = findViewById(R.id.swiperefresh);
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh () {
				apiDataViewModel.loadFeeds("");
			}
		});

		// Initialize ViewModel
		apiDataViewModel = ViewModelProviders.of(this).get(ApiDataViewModel.class);
		apiDataViewModel.getLiveData().observe(this, new Observer<DataWrapper<FlickrFeed>>() {
			@Override
			public void onChanged (@Nullable final DataWrapper<FlickrFeed> flickrFeedDataWrapper) {

				// Update adapter
				feedAdapter.updateData(flickrFeedDataWrapper.getData());

				// Update status
				switch (flickrFeedDataWrapper.getStatus()){
					case NONE:
						swipeRefreshLayout.setRefreshing(false);
						break;
					case LOADING:
						swipeRefreshLayout.setRefreshing(true);
						break;
					case ERROR:
						Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, BaseTransientBottomBar.LENGTH_SHORT);
						break;
				}
			}
		});
	}

}
