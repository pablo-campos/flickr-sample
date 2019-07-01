package com.pablocampos.flickrsample.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.pablocampos.flickrsample.R;
import com.pablocampos.flickrsample.adapter.FeedAdapter;
import com.pablocampos.flickrsample.adapter.FeedClickListener;
import com.pablocampos.flickrsample.adapter.FeedItemAnimator;
import com.pablocampos.flickrsample.model.ApiDataViewModel;
import com.pablocampos.flickrsample.model.DataWrapper;
import com.pablocampos.flickrsample.model.FlickrFeed;
import com.pablocampos.flickrsample.utils.Utils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
				if (Utils.checkInternet(FlickrActivity.this)){		// Check internet connection and then perform query
					apiDataViewModel.loadFeeds(query);
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

		FirebaseApp.initializeApp(this);

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
