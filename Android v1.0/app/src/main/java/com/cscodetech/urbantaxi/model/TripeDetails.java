package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class TripeDetails {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("RideDetails")
	private RideDetails rideDetails;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public RideDetails getRideDetails(){
		return rideDetails;
	}

	public String getResult(){
		return result;
	}
}