package activity

import adapter.FeedAdapter
import adapter.FeedClickListener
import adapter.FeedItemAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import com.pablocampos.flickrsample.R
import model.ApiDataViewModel
import model.DataWrapper
import model.FlickrFeed
import utils.Utils

@Suppress("UNNECESSARY_NOT_NULL_ASSERTION")
class FlickrActivity : AppCompatActivity() {


	// LiveData ViewModel
	private lateinit var apiDataViewModel: ApiDataViewModel

	// Adapters and views
	private lateinit var searchView: SearchView
	private lateinit var swipeRefreshLayout: SwipeRefreshLayout
	private lateinit var recyclerView: RecyclerView
	private lateinit var feedAdapter: FeedAdapter


	override fun onCreateOptionsMenu(menu: Menu): Boolean {

		menuInflater.inflate(R.menu.menu_search, menu)

		// Initialize the search functionality on the activity's toolbar
		val searchActionItem = menu.findItem(R.id.action_search)
		searchView = searchActionItem.actionView as SearchView

		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String): Boolean {

				// If search view is empty, let's update the adapter with zero items, if not, let's request a new search query:
				if (Utils.checkInternet(this@FlickrActivity)) {        // Check internet connection and then perform query
					apiDataViewModel!!.loadFeeds(this@FlickrActivity, query)
				}

				// Update view
				if (!searchView.isIconified) {
					searchView.isIconified = true
				}
				searchActionItem.collapseActionView()
				return false
			}

			override fun onQueryTextChange(query: String): Boolean {
				return false
			}
		})

		return true
	}


	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		// When the user selects an option to see the licenses:
		if (item.itemId == R.id.action_settings) {
			OssLicensesMenuActivity.setActivityTitle(getString(R.string.action_bar_open_source_licences_title))
			startActivity(Intent(this@FlickrActivity, OssLicensesMenuActivity::class.java))
		}

		return super.onOptionsItemSelected(item)
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_flickr)

		// Initialize grid view
		val feedClickListener = object : FeedClickListener {
			override fun onClick(view: View, flickrFeed: FlickrFeed) {
				val intent = Intent(this@FlickrActivity, DetailsActivity::class.java)
				intent.putExtra(DetailsActivity.FLICKR_FEED, flickrFeed)
				val options = ActivityOptions.makeSceneTransitionAnimation(this@FlickrActivity, view, "feed_image")
				startActivity(intent, options.toBundle())
			}
		}

		feedAdapter = FeedAdapter(feedClickListener)

		recyclerView = findViewById(R.id.feedGrid)
		recyclerView.setHasFixedSize(true)
		recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
		recyclerView.itemAnimator = FeedItemAnimator()
		recyclerView.adapter = feedAdapter

		// Initialize swipe to refresh
		swipeRefreshLayout = findViewById(R.id.swiperefresh)
		swipeRefreshLayout.setOnRefreshListener { apiDataViewModel.loadFeeds(this@FlickrActivity, "") }

		// Initialize ViewModel
		apiDataViewModel = ViewModelProviders.of(this).get(ApiDataViewModel::class.java)
		apiDataViewModel.getLiveData(this).observe(this, Observer<DataWrapper<FlickrFeed>> { flickrFeedDataWrapper ->

			// Update adapter
			feedAdapter.updateData(flickrFeedDataWrapper.data!!)

			// Update status
			when (flickrFeedDataWrapper.status) {
				DataWrapper.Status.IDDLE -> swipeRefreshLayout.isRefreshing = false
				DataWrapper.Status.LOADING -> swipeRefreshLayout.isRefreshing = true
				DataWrapper.Status.ERROR -> {
					swipeRefreshLayout.isRefreshing = false
					Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, Snackbar.LENGTH_SHORT)
				}
			}
		})
	}

}
