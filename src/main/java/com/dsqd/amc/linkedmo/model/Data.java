package com.dsqd.amc.linkedmo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;

@Getter
@Setter
public class Data {
    private int id;
    private String stringValue;
    private Integer integerValue;
    private Double doubleValue;
    private LocalDate dateValue;
    private LocalTime timeValue;
    	
	public void parse(JSONObject jsonObject) {
		this.setStringValue(jsonObject.getAsString("stringValue"));
		this.setIntegerValue(jsonObject.getAsNumber("integerValue").intValue());
		this.setDoubleValue(jsonObject.getAsNumber("doubleValue").doubleValue());
		this.setDateValue(LocalDate.parse(jsonObject.getAsString("dateValue")));
		this.setTimeValue(LocalTime.parse(jsonObject.getAsString("timeValue")));
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
