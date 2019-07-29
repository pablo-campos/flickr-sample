package model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import network.FlickrApi
import network.FlickrInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ApiDataViewModel : ViewModel() {


	private var dataWrapper: DataWrapper<FlickrFeed>? = null
	private var liveData: MutableLiveData<DataWrapper<FlickrFeed>>? = null


	fun getLiveData(context: Context): MutableLiveData<DataWrapper<FlickrFeed>> {
		if (liveData == null) {
			dataWrapper = DataWrapper()
			liveData = MutableLiveData()
			loadFeeds(context, "")
		}
		return liveData as MutableLiveData<DataWrapper<FlickrFeed>>
	}


	fun loadFeeds(context: Context, tags: String) {

		val service = FlickrApi.getClient(context).create(FlickrInterface::class.java)
		val apiCall = service.getFlickrFeeds(tags)

		dataWrapper!!.status = DataWrapper.Status.LOADING
		liveData!!.postValue(dataWrapper)

		apiCall.enqueue(object : Callback<ApiData> {
			override fun onResponse(call: Call<ApiData>, response: Response<ApiData>) {

				// Stop refresh status and update data
				dataWrapper!!.status = DataWrapper.Status.IDDLE
				dataWrapper!!.data = response.body()!!.items
				liveData!!.postValue(dataWrapper)
			}

			override fun onFailure(call: Call<ApiData>, t: Throwable) {

				// Stop refresh status
				dataWrapper!!.status = DataWrapper.Status.ERROR
				dataWrapper!!.data = ArrayList()
				liveData!!.postValue(dataWrapper)
			}
		})
	}
}
