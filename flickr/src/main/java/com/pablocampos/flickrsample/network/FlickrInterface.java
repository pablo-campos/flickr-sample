package com.pablocampos.flickrsample.network;

import com.pablocampos.flickrsample.model.ApiData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FlickrInterface {

	@GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=?")
	Call<ApiData> getFlickrFeeds (@Query("tags") String tags);
}
