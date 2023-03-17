package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payout{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Payoutlist")
	private List<PayoutlistItem> payoutlist;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<PayoutlistItem> getPayoutlist(){
		return payoutlist;
	}

	public String getResult(){
		return result;
	}
}