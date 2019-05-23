package com.pablocampos.flickrsample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Media implements Serializable {

	@SerializedName("m")
	@Expose
	private String m;

	public String getM() {

		if (m.contains("_m.")){
			return m.replace("_m.", ".");
		}
		return m;
	}

	public void setM(String m) {
		this.m = m;
	}

}