package com.pablocampos.flickrsample.model;

import com.pablocampos.flickrsample.network.FlickrApi;
import com.pablocampos.flickrsample.network.FlickrInterface;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiDataViewModel extends ViewModel {


	private DataWrapper<FlickrFeed> dataWrapper;
	private MutableLiveData<DataWrapper<FlickrFeed>> liveData;



	public MutableLiveData<DataWrapper<FlickrFeed>> getLiveData () {
		if (liveData == null){
			dataWrapper = new DataWrapper<>();
			liveData = new MutableLiveData<>();
			loadFeeds("");
		}
		return liveData;
	}



	public void loadFeeds(final String tags){

		FlickrInterface service = FlickrApi.getClient().create(FlickrInterface.class);
		Call<ApiData> apiCall = service.getFlickrFeeds(tags);

		dataWrapper.setStatus(DataWrapper.Status.LOADING);
		liveData.postValue(dataWrapper);

		apiCall.enqueue(new Callback<ApiData>() {
			@Override
			public void onResponse(Call<ApiData> call, Response<ApiData> response) {

				// Stop refresh status and update data
				dataWrapper.setStatus(DataWrapper.Status.NONE);
				dataWrapper.setData(response.body().getItems());
				liveData.postValue(dataWrapper);
			}

			@Override
			public void onFailure(Call<ApiData> call, Throwable t) {

				// Stop refresh status
				dataWrapper.setStatus(DataWrapper.Status.ERROR);
				dataWrapper.setData(new ArrayList<FlickrFeed>());
				liveData.postValue(dataWrapper);
			}
		});
	}
}
