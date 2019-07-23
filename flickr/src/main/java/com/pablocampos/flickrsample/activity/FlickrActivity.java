package com.pablocampos.flickrsample.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.snackbar.Snackbar;
import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.adapter.FeedAdapter;
import com.pablocampos.flickrsample.adapter.FeedClickListener;
import com.pablocampos.flickrsample.adapter.FeedItemAnimator;
import com.pablocampos.flickrsample.model.ApiDataViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import utils.Utils;

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
		final MenuItem searchActionItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) searchActionItem.getActionView();

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {

				// If search view is empty, let's update the adapter with zero items, if not, let's request a new search query:
				if (Utils.INSTANCE.checkInternet(FlickrActivity.this)){		// Check internet connection and then perform query
					apiDataViewModel.loadFeeds(FlickrActivity.this, query);
				}

				// Update view
				if(!searchView.isIconified()) {
					searchView.setIconified(true);
				}
				searchActionItem.collapseActionView();
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
	public boolean onOptionsItemSelected (final MenuItem item) {

		// When the user selects an option to see the licenses:
		if (item.getItemId() == R.id.action_settings){
			OssLicensesMenuActivity.setActivityTitle(getString(R.string.action_bar_open_source_licences_title));
			startActivity(new Intent(FlickrActivity.this, OssLicensesMenuActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_flickr);

		// Initialize grid view
		final FeedClickListener feedClickListener = (view, flickrFeed) -> {

			Intent intent = new Intent(FlickrActivity.this, DetailsActivity.class);
			intent.putExtra(DetailsActivity.FLICKR_FEED, flickrFeed);
			ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FlickrActivity.this, view, "feed_image");
			startActivity(intent, options.toBundle());
		};

		feedAdapter = new FeedAdapter(feedClickListener);

		recyclerView = findViewById(R.id.feedGrid);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
		recyclerView.setItemAnimator(new FeedItemAnimator());
		recyclerView.setAdapter(feedAdapter);

		// Initialize swipe to refresh
		swipeRefreshLayout = findViewById(R.id.swiperefresh);
		swipeRefreshLayout.setOnRefreshListener(() -> apiDataViewModel.loadFeeds(FlickrActivity.this, ""));

		// Initialize ViewModel
		apiDataViewModel = ViewModelProviders.of(this).get(ApiDataViewModel.class);
		apiDataViewModel.getLiveData(this).observe(this, flickrFeedDataWrapper -> {

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
					swipeRefreshLayout.setRefreshing(false);
					Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, Snackbar.LENGTH_SHORT);
					break;
			}
		});
	}

}
