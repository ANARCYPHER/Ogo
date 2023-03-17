package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class VehicleDataItem{

	@SerializedName("img")
	private String img;

	@SerializedName("uprice")
	private String uprice;

	@SerializedName("ukms")
	private String ukms;

	@SerializedName("id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("aprice")
	private String aprice;

	@SerializedName("status")
	private String status;

	public String getImg(){
		return img;
	}

	public String getUprice(){
		return uprice;
	}

	public String getUkms(){
		return ukms;
	}

	public String getId(){
		return id;
	}

	public String getTitle(){
		return title;
	}

	public String getAprice(){
		return aprice;
	}

	public String getStatus(){
		return status;
	}
}