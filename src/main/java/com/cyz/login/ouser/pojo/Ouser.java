package com.cyz.login.ouser.pojo;

import java.io.Serializable;

import com.cyz.basic.pojo.DeleteAbleEntity;

public class Ouser extends DeleteAbleEntity<Integer> implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1809896140253877301L;

	private String username;
	
	private String password;
	
	

	public Ouser() {
		super();
		// TODO Auto-generated constructor stub
	}



	public Ouser(Integer id, Byte delflag) {
		super(id, delflag);
		// TODO Auto-generated constructor stub
	}



	public Ouser(Integer id) {
		super(id);
	}

    

	public Ouser(String username) {
		this.username = username;
	}



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



	@Override
	public void acceptId(long id) {
		this.id = Integer.parseInt(String.valueOf(id));
	}

}
