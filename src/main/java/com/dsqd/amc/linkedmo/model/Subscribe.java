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
	private String offercode;

	public Subscribe() {
	}

	// Getter & Setter
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

	public String getOffercode() {
		return offercode;
	}

	public void setOffercode(String offercode) {
		this.offercode = offercode;
	}

	// Builder
	public Subscribe(Builder builder) {
		this.id = builder.id;
		this.spcode = builder.spcode;
		this.mobileno = builder.mobileno;
		this.agree1 = builder.agree1;
		this.agree2 = builder.agree2;
		this.agree3 = builder.agree3;
		this.bFP = builder.bFP;
		this.status = builder.status;
		this.offercode = builder.offercode;
	}

	public static class Builder {
		private int id;
		private String spcode;
		private String mobileno;
		private boolean agree1;
		private boolean agree2;
		private boolean agree3;
		private String bFP;
		private String status;
		private String offercode;

		public Builder(String mobileno) { // 필수 매개변수 생성자
			this.mobileno = mobileno;
		}

		public Builder id(int id) {
			this.id = id;
			return this;
		}

		public Builder spcode(String spcode) {
			this.spcode = spcode;
			return this;
		}

		public Builder mobileno(String mobileno) {
			this.mobileno = mobileno;
			return this;
		}

		public Builder agree1(boolean agree1) {
			this.agree1 = agree1;
			return this;
		}

		public Builder agree2(boolean agree2) {
			this.agree2 = agree2;
			return this;
		}

		public Builder agree3(boolean agree3) {
			this.agree3 = agree3;
			return this;
		}

		public Builder bFP(String bFP) {
			this.bFP = bFP;
			return this;
		}

		public Builder status(String status) {
			this.status = status;
			return this;
		}

		public Builder offercode(String offercode) {
			this.offercode = offercode;
			return this;
		}

		public Subscribe build() {
			return new Subscribe(this);
		}
	}

}
