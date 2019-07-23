package network

import model.ApiData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrInterface {

	@GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=?")
	fun getFlickrFeeds(@Query("tags") tags: String): Call<ApiData>
}
