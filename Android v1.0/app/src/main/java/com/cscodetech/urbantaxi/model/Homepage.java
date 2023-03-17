package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Homepage{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("OrderHistory")
	private List<OrderHistoryItem> orderHistory;

	@SerializedName("partner_data")
	private PartnerData partnerData;

	@SerializedName("Result")
	private String result;

	@SerializedName("currency")
	private String currency;

	@SerializedName("app_lan")
	private String appLan;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<OrderHistoryItem> getOrderHistory(){
		return orderHistory;
	}

	public PartnerData getPartnerData(){
		return partnerData;
	}

	public String getResult(){
		return result;
	}

	public String getCurrency() {
		return currency;
	}

	public String getAppLan() {
		return appLan;
	}
}