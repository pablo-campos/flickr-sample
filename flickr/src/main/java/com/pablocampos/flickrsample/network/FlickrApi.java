package com.pablocampos.flickrsample.network;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlickrApi {


	private static final String BASE_URL = "https://api.flickr.com";

	private static Retrofit retrofit;





	public static Retrofit getClient(final Context context) {

		if (retrofit == null) {

			OkHttpClient client = new OkHttpClient.Builder()
					.addInterceptor(new ChuckInterceptor(context))
					.build();

			retrofit = new retrofit2.Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.client(client)
					.build();
		}

		return retrofit;
	}

}
