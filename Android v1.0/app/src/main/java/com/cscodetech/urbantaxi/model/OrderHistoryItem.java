package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class OrderHistoryItem{

	@SerializedName("ride_id")
	private String rideId;

	@SerializedName("order_date")
	private String orderDate;

	@SerializedName("o_status")
	private String oStatus;

	@SerializedName("pick_long")
	private String pickLong;

	@SerializedName("pick_lat")
	private String pickLat;

	@SerializedName("drop_lat")
	private String dropLat;

	@SerializedName("pick_address")
	private String pickAddress;

	@SerializedName("drop_address")
	private String dropAddress;

	@SerializedName("order_total")
	private String orderTotal;

	@SerializedName("car_img")
	private String carImg;

	@SerializedName("drop_long")
	private String dropLong;

	@SerializedName("car_type")
	private String carType;

	public String getRideId(){
		return rideId;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public String getOStatus(){
		return oStatus;
	}

	public String getPickLong(){
		return pickLong;
	}

	public String getPickLat(){
		return pickLat;
	}

	public String getDropLat(){
		return dropLat;
	}

	public String getPickAddress(){
		return pickAddress;
	}

	public String getDropAddress(){
		return dropAddress;
	}

	public String getOrderTotal(){
		return orderTotal;
	}

	public String getCarImg(){
		return carImg;
	}

	public String getDropLong(){
		return dropLong;
	}

	public String getCarType(){
		return carType;
	}
}