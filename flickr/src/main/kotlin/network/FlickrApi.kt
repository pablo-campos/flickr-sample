package network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FlickrApi {


	private val BASE_URL = "https://api.flickr.com"

	private var retrofit: Retrofit? = null


	fun getClient(context: Context): Retrofit {

		if (retrofit == null) {

			val client = OkHttpClient.Builder()
					.addInterceptor(ChuckerInterceptor(context))
					.build()

			retrofit = retrofit2.Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.client(client)
					.build()
		}

		return retrofit as Retrofit
	}

}
