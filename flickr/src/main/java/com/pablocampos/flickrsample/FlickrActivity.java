package com.pablocampos.flickrsample;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pablocampos.flickrsample.model.ApiData;
import com.pablocampos.flickrsample.network.FlickrApi;
import com.pablocampos.flickrsample.network.FlickrInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlickrActivity extends AppCompatActivity {


	private Call<ApiData> apiCall;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flickr);

		performQuery("puppy");
	}



	private void performQuery(final String query){

		FlickrInterface service = FlickrApi.getClient().create(FlickrInterface.class);

		apiCall = service.getFlickrFeeds(query);
		apiCall.enqueue(new Callback<ApiData>() {
			@Override
			public void onResponse(Call<ApiData> call, Response<ApiData> response) {
				int x = 0;
			}

			@Override
			public void onFailure(Call<ApiData> call, Throwable t) {

				// Display an error
				Snackbar.make(findViewById(android.R.id.content), R.string.network_call_error, BaseTransientBottomBar.LENGTH_SHORT);
			}
		});
	}

}
