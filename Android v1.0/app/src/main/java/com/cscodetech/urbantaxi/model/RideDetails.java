package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class RideDetails {

	@SerializedName("transaction_id")
	private String transactionId;

	@SerializedName("wall_amt")
	private String wallAmt;

	@SerializedName("p_method_name")
	private String pMethodName;

	@SerializedName("pick_long")
	private double pickLong;

	@SerializedName("cou_amt")
	private String couAmt;

	@SerializedName("o_status")
	private String oStatus;

	@SerializedName("comment_reject")
	private String commentReject;

	@SerializedName("orderid")
	private String orderid;

	@SerializedName("drop_address")
	private String dropAddress;

	@SerializedName("rider_img")
	private String riderImg;

	@SerializedName("total_amt")
	private String totalAmt;

	@SerializedName("order_date")
	private String orderDate;

	@SerializedName("pick_lat")
	private Double pickLat;

	@SerializedName("rider_name")
	private String riderName;

	@SerializedName("pick_time")
	private String pickTime;

	@SerializedName("pick_address")
	private String pickAddress;

	@SerializedName("drop_time")
	private String dropTime;

	@SerializedName("rider_mobile")
	private String riderMobile;

	@SerializedName("car_type")
	private String carType;

	@SerializedName("car_img")
	private String carImg;

	@SerializedName("drop_lat")
	private double dropLat;

	@SerializedName("drop_long")
	private double dropLong;

	public String getTransactionId(){
		return transactionId;
	}

	public String getWallAmt(){
		return wallAmt;
	}

	public String getPMethodName(){
		return pMethodName;
	}

	public double getPickLong(){
		return pickLong;
	}

	public String getCouAmt(){
		return couAmt;
	}

	public String getOStatus(){
		return oStatus;
	}

	public Object getCommentReject(){
		return commentReject;
	}

	public String getOrderid(){
		return orderid;
	}

	public String getDropAddress(){
		return dropAddress;
	}

	public String getRiderImg(){
		return riderImg;
	}

	public String getTotalAmt(){
		return totalAmt;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public Double getPickLat(){
		return pickLat;
	}

	public String getRiderName(){
		return riderName;
	}

	public Object getPickTime(){
		return pickTime;
	}

	public String getPickAddress(){
		return pickAddress;
	}

	public Object getDropTime(){
		return dropTime;
	}

	public String getRiderMobile(){
		return riderMobile;
	}

	public String getCarType() {
		return carType;
	}

	public String getCarImg() {
		return carImg;
	}

	public double getDropLat() {
		return dropLat;
	}

	public double getDropLong() {
		return dropLong;
	}
}