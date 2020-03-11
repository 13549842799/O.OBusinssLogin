package com.cyz.login.ouser.pojo;

import java.time.LocalDate;

public class WebMessage {

	private int id;
	private String code;
	private String name;
	private String homepage;
	private String signoutAddress;
	private LocalDate jointime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public LocalDate getJointime() {
		return jointime;
	}

	public void setJointime(LocalDate jointime) {
		this.jointime = jointime;
	}

	public String getSignoutAddress() {
		return signoutAddress;
	}

	public void setSignoutAddress(String signoutAddress) {
		this.signoutAddress = signoutAddress;
	}

}
