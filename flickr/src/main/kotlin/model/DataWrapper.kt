package model

import java.util.*

class DataWrapper<T> {


	var status: Status? = null
	var data: List<T>? = null


	enum class Status {
		IDDLE,
		LOADING,
		ERROR
	}

	init {
		data = ArrayList()
	}
}
