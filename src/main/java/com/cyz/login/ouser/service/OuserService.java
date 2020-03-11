package com.cyz.login.ouser.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cyz.basic.constant.EntityConstants;
import com.cyz.basic.net.SessionInfo;
import com.cyz.basic.service.impl.BasicServiceImplTemplate;
import com.cyz.basic.util.PassUtil;
import com.cyz.login.ouser.pojo.LogOutForm;
import com.cyz.login.ouser.pojo.Ouser;
import com.cyz.login.ouser.pojo.WebMessage;

@Service
public class OuserService extends BasicServiceImplTemplate<Ouser>{
	
	@Autowired
	private RedisService redisService;

	@Override
	public Ouser newEntity() {
		
		return new Ouser();
	}

	public boolean checkPasswordValid(String dbPassowrd , String password) {
		try {
			return PassUtil.validPassword(password,dbPassowrd);		
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} 	
	}
	
	/**
	 * 实际的key:sessionInfo::#tokenKey
	 * @param user
	 * @param web
	 * @param tokenKey
	 * @return
	 */
	//@CachePut(value="sessionInfo", key="#type + '_' + #username")
	@CachePut(value="sessionInfo", key="#tokenKey")
	public SessionInfo saveSessonInfo(Ouser user, WebMessage web, String tokenKey) {
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setId(user.getId());
		sessionInfo.setUsername(user.getUsername());
		sessionInfo.addResource(web.getCode(), web.getSignoutAddress());
		sessionInfo.setToken(redisService.createToken(null));
		sessionInfo.setAvailableDate(LocalDateTime.now().plusSeconds(redisService.getExpires().get(RedisService.KeyPrefix.sessionInfo.name())).toInstant(ZoneOffset.of("+8")).toEpochMilli());		
		return sessionInfo;
	}
	
	
	/**
	 * 测试方法，可删除
	 * @param user
	 * @param abc
	 * @return
	 */
	//@Cacheable(value="userInfo", key="#user.username + '_' + #abc")
	public String testCache(Ouser user, String abc) {
		
		return "";
	}

	public boolean logOut(Ouser user, String token, String tokenKey) {
		String key = redisService.completeKey(RedisService.KeyPrefix.sessionInfo, tokenKey);
		boolean hasKey = redisService.hasKey(key);
        if (hasKey) {        	
        	SessionInfo obj = (SessionInfo)redisService.get(key);
        	if (obj.getResourceList() != null && obj.getResourceList().size() > 0) {
        		
        	}
        }
		return false;
	}
}
