package com.pubhealth.entity;

import java.text.SimpleDateFormat;

import org.elasticsearch.action.fieldstats.FieldStats.Date;

public class HealthEdu extends Page {
	private int provinceId;
	private int cityId;
	private int districtId;
	private String personalName;
	private String idCard;
	
	private int id;
	
	private String fromTime;
	private String toTime;
	
	
	
	public HealthEdu() {
		super();
		id = -1;
	}
	public int getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getDistrictId() {
		return districtId;
	}
	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}
	public String getPersonalName() {
		return personalName;
	}
	public void setPersonalName(String personalName) {
		this.personalName = personalName;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	
}