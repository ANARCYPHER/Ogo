package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class ReportData{

	@SerializedName("total_earning")
	private String totalEarning;

	@SerializedName("withdraw_limit")
	private int withdrawLimit;

	@SerializedName("payout")
	private int payout;

	public String getTotalEarning(){
		return totalEarning;
	}

	public int getWithdrawLimit(){
		return withdrawLimit;
	}

	public int getPayout(){
		return payout;
	}
}