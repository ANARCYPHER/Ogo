package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tripe {

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("OrderHistory")
	private List<OrderHistoryItem> orderHistory;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<OrderHistoryItem> getOrderHistory(){
		return orderHistory;
	}

	public String getResult(){
		return result;
	}
}