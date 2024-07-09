package com.dsqd.amc.linkedmo.model;

import java.util.Date;

public class Subscribe {
	private int id;
	private String spcode;
	private String mobileno;
	private boolean agree1;
	private boolean agree2;
	private boolean agree3;
	private Date createDate;
	private String bFP;
	private String status; 
	
	//Getter & Setter
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public boolean getAgree1() {
		return agree1;
	}
	public void setAgree1(boolean agree1) {
		this.agree1 = agree1;
	}
	public boolean getAgree2() {
		return agree2;
	}
	public void setAgree2(boolean agree2) {
		this.agree2 = agree2;
	}
	public boolean getAgree3() {
		return agree3;
	}
	public void setAgree3(boolean agree3) {
		this.agree3 = agree3;
	}
	public String getbFP() {
		return bFP;
	}
	public void setbFP(String bFP) {
		this.bFP = bFP;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
