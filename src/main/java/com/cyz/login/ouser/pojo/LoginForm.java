package com.cyz.login.ouser.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3713428028783120304L;
	
	private String code;

	private String username;
	
	private String password;
	
	private String validCode;
		
	@JsonProperty(required=true)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonProperty(required=true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty(required=true)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}
	
	

}
