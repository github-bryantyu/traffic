package com.example.demo;

import javax.persistence.Entity;

import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tsm")
public class TSMRow {
	
	@Id
	private String linkId;
	
	private String region;
	
	private String roadType;
	
	private String roadSaturationLevel;
	
	private int trafficSpeed;
	
	private Timestamp captureDate;
	
	public String getLinkId() {
		return linkId;
	}
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	public String getRoadType() {
		return roadType;
	}
	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	
	public String getRoadSaturationLevel() {
		return roadSaturationLevel;
	}
	public void setRoadSaturationLevel(String roadSaturationLevel) {
		this.roadSaturationLevel = roadSaturationLevel;
	}
	
	public int getTrafficSpeed() {
		return trafficSpeed;
	}
	public void setTrafficSpeed(int trafficSpeed) {
		this.trafficSpeed = trafficSpeed;
	}
	
	public Timestamp getCaptureDate() {
		return captureDate;
	}
	public void setCaptureDate(Timestamp captureDate) {
		this.captureDate = captureDate;
	} 

}
