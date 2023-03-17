package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Contry{

	@SerializedName("ResponseCode")
	private String responseCode;

	@SerializedName("ResponseMsg")
	private String responseMsg;

	@SerializedName("CountryCode")
	private List<CountryCodeItem> countryCode;

	@SerializedName("Result")
	private String result;

	public String getResponseCode(){
		return responseCode;
	}

	public String getResponseMsg(){
		return responseMsg;
	}

	public List<CountryCodeItem> getCountryCode(){
		return countryCode;
	}

	public String getResult(){
		return result;
	}
}