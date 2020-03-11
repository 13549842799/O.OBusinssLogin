package com.cyz.login.ouser.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cyz.login.ouser.mapper.WebMessageMapper;
import com.cyz.login.ouser.pojo.WebMessage;

@Service
public class WebMessageService {
	
	@Autowired
	private WebMessageMapper mapper;
	
	public WebMessage getByCode(String code) {
		if (StringUtils.isEmpty(code)) {
			return null;
		}
		return mapper.getByCode(code);
	}
	
	public boolean hasWebMessage(String code) {
		return getByCode(code) != null;
	}

}
