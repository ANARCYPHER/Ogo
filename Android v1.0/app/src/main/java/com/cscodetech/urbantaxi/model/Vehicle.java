package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vehicle{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("VehicleData")
	private List<VehicleDataItem> vehicleData;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<VehicleDataItem> getVehicleData(){
		return vehicleData;
	}

	public String getResult(){
		return result;
	}
}