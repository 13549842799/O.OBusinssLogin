package com.cyz.login.ouser.pojo;

import java.io.Serializable;

public class LogOutForm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8159007154475342069L;

	private String username;
	
	private String token;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	

}
