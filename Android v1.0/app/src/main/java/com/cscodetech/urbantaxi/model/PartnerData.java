package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class PartnerData{

	@SerializedName("license_back")
	private String licenseBack;

	@SerializedName("identity_back")
	private String identityBack;

	@SerializedName("license_front")
	private String licenseFront;

	@SerializedName("identity_front")
	private String identityFront;

	@SerializedName("is_approve")
	private String isApprove;

	@SerializedName("identity_status")
	private String identityStatus;

	@SerializedName("license_status")
	private String licenseStatus;

	public String getLicenseBack(){
		return licenseBack;
	}

	public String getIdentityBack(){
		return identityBack;
	}

	public String getLicenseFront(){
		return licenseFront;
	}

	public String getIdentityFront(){
		return identityFront;
	}

	public String getIsApprove(){
		return isApprove;
	}

	public String getIdentityStatus() {
		return identityStatus;
	}

	public String getLicenseStatus() {
		return licenseStatus;
	}
}