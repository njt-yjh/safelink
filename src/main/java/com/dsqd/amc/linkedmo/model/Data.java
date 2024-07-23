package com.dsqd.amc.linkedmo.model;

import java.time.LocalDate;
import java.time.LocalTime;
import org.apache.ibatis.annotations.*;

import net.minidev.json.JSONObject;

public class Data {
    private int id;
    private String stringValue;
    private Integer integerValue;
    private Double doubleValue;
    private LocalDate dateValue;
    private LocalTime timeValue;
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public Integer getIntegerValue() {
		return integerValue;
	}
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}
	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public LocalDate getDateValue() {
		return dateValue;
	}
	public void setDateValue(LocalDate dateValue) {
		this.dateValue = dateValue;
	}
	public LocalTime getTimeValue() {
		return timeValue;
	}
	public void setTimeValue(LocalTime timeValue) {
		this.timeValue = timeValue;
	}

    // Getters and Setters
	
	public void parse(JSONObject jsonObject) {
		setStringValue(jsonObject.getAsString("stringValue"));
		setIntegerValue(jsonObject.getAsNumber("integerValue").intValue());
		setDoubleValue(jsonObject.getAsNumber("doubleValue").doubleValue());
		setDateValue(LocalDate.parse(jsonObject.getAsString("dateValue")));
		setTimeValue(LocalTime.parse(jsonObject.getAsString("timeValue")));
	}
	
	public String toJSONString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("stringValue", getStringValue());
		jsonObject.put("integerValue", getIntegerValue());
		jsonObject.put("doubleValue", getDoubleValue());
		jsonObject.put("dateValue", java.sql.Date.valueOf(getDateValue()));
		jsonObject.put("timeValue", getTimeValue().toString());
		
		return jsonObject.toJSONString();
	}
    
}
