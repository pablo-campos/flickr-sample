package model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.io.Serializable

class Media : Serializable {

	@SerializedName("m")
	@Expose
	var m: String? = null
		get() = if (field!!.contains("_m.")) {
			field!!.replace("_m.", ".")
		} else field

}