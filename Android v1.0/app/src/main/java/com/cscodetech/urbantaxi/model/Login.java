package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class Login{

	@SerializedName("RiderData")
	private RiderData riderData;

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public RiderData getRiderData(){
		return riderData;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}