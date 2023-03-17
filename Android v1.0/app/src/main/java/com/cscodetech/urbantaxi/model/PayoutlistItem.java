package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class PayoutlistItem{

	@SerializedName("payout_id")
	private String payoutId;

	@SerializedName("amt")
	private String amt;

	@SerializedName("r_date")
	private String rDate;

	@SerializedName("proof")
	private Object proof;

	@SerializedName("r_type")
	private String rType;

	@SerializedName("status")
	private String status;

	public String getPayoutId(){
		return payoutId;
	}

	public String getAmt(){
		return amt;
	}

	public String getRDate(){
		return rDate;
	}

	public Object getProof(){
		return proof;
	}

	public String getRType(){
		return rType;
	}

	public String getStatus(){
		return status;
	}
}