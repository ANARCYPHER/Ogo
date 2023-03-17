package com.cscodetech.urbantaxi.model;

import com.google.gson.annotations.SerializedName;

public class RiderData{

	@SerializedName("fname")
	private String fname;

	@SerializedName("address")
	private String address;

	@SerializedName("gender")
	private String gender;

	@SerializedName("mobile")
	private String mobile;

	@SerializedName("license_front")
	private String licenseFront;

	@SerializedName("identity_front")
	private String identityFront;

	@SerializedName("license_back")
	private String licenseBack;

	@SerializedName("ccode")
	private String ccode;

	@SerializedName("reg_date")
	private String regDate;

	@SerializedName("lname")
	private String lname;

	@SerializedName("password")
	private String password;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("identity_back")
	private String identityBack;

	@SerializedName("pro_img")
	private String proImg;

	@SerializedName("id")
	private String id;

	@SerializedName("is_online")
	private String isOnline;

	@SerializedName("email")
	private String email;

	@SerializedName("is_approve")
	private String isApprove;

	@SerializedName("status")
	private String status;

	@SerializedName("car_img")
	private String carimage;

	@SerializedName("car_type")
	private String carTyep;

	public String getFname(){
		return fname;
	}

	public String getAddress(){
		return address;
	}

	public String getGender(){
		return gender;
	}

	public String getMobile(){
		return mobile;
	}

	public String getLicenseFront(){
		return licenseFront;
	}

	public String getIdentityFront(){
		return identityFront;
	}

	public String getLicenseBack(){
		return licenseBack;
	}

	public String getCcode(){
		return ccode;
	}

	public String getRegDate(){
		return regDate;
	}

	public String getLname(){
		return lname;
	}

	public String getPassword(){
		return password;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public String getIdentityBack(){
		return identityBack;
	}

	public String getProImg(){
		return proImg;
	}

	public String getId(){
		return id;
	}

	public String getIsOnline(){
		return isOnline;
	}

	public String getEmail(){
		return email;
	}

	public String getIsApprove(){
		return isApprove;
	}

	public String getStatus(){
		return status;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setLicenseFront(String licenseFront) {
		this.licenseFront = licenseFront;
	}

	public void setIdentityFront(String identityFront) {
		this.identityFront = identityFront;
	}

	public void setLicenseBack(String licenseBack) {
		this.licenseBack = licenseBack;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public void setIdentityBack(String identityBack) {
		this.identityBack = identityBack;
	}

	public void setProImg(String proImg) {
		this.proImg = proImg;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setIsApprove(String isApprove) {
		this.isApprove = isApprove;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCarimage() {
		return carimage;
	}

	public void setCarimage(String carimage) {
		this.carimage = carimage;
	}

	public String getCarTyep() {
		return carTyep;
	}

	public void setCarTyep(String carTyep) {
		this.carTyep = carTyep;
	}
}