package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class Withdraw{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("report_data")
	private ReportData reportData;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public ReportData getReportData(){
		return reportData;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public String getResult(){
		return result;
	}
}