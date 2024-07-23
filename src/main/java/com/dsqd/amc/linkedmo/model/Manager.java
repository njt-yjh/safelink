package com.dsqd.amc.linkedmo.model;

import com.dsqd.amc.linkedmo.model.Subscribe.Builder;

public class Manager {
	
	private String username;
	private String password;
	
	public Manager() {}
	
	// Getter & Setter
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	//Builder 
	public Manager(Builder builder) {
		this.username = builder.username;
		this.password = builder.password;
	}
	
    public static class Builder {
    	private String username;
    	private String password;

        public Builder(String username) { // 필수 매개변수 생성자
        	this.username = username;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }
    }
	
}
