package com.dsqd.amc.linkedmo.model;

import java.util.Date;

import com.dsqd.amc.linkedmo.model.Manager.Builder;

public class Batch {
	private int txid;
	private String batchid;
	private Date started;
	private Date ended;
	private String code;
	private String result;
	
	public Batch() {}
	
	// Getter & Setter
    public int getTxid() {
		return txid;
	}
	public void setTxid(int txid) {
		this.txid = txid;
	}
	public String getBatchid() {
		return batchid;
	}
	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}
	public Date getStarted() {
		return started;
	}
	public void setStarted(Date started) {
		this.started = started;
	}
	public Date getEnded() {
		return ended;
	}
	public void setEnded(Date ended) {
		this.ended = ended;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	//Builder 
	public Batch(Builder builder) {
		this.batchid = builder.batchid;
		this.started = builder.started;
		this.ended = builder.ended;
		this.code = builder.code;
		this.result = builder.result;
	}
	


	public static class Builder {
		private String batchid;
		private Date started;
		private Date ended;
		private String code;
		private String result;

        public Builder(String batchid) { // 필수 매개변수 생성자
        	this.batchid = batchid;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }
        
        public Builder result(String result) {
            this.result = result;
            return this;
        }
        
        public Builder started(Date started) {
            this.started = started;
            return this;
        }
        
        public Builder ended(Date ended) {
            this.ended = ended;
            return this;
        }
	}
}
