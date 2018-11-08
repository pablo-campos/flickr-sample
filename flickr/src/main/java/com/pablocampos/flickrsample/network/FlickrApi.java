package com.pablocampos.flickrsample.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrApi {


	private static final String BASE_URL = "https://api.flickr.com";

	private static Retrofit retrofit;





	public static Retrofit getClient() {

		if (retrofit == null) {

			retrofit = new retrofit2.Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
		}

		return retrofit;
	}

}
