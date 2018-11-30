package com.pablocampos.flickrsample.model;

import java.util.List;

public class DataWrapper <T> {


	public enum Status {
		NONE,
		LOADING,
		ERROR
	}


	private Status status;
	private List<T> data;



	public Status getStatus () {
		return status;
	}



	public void setStatus (final Status status) {
		this.status = status;
	}



	public List<T> getData () {
		return data;
	}



	public void setData (final List<T> data) {
		this.data = data;
	}
}
