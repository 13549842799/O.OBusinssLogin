package com.cyz.login.ouser.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.cyz.basic.pojo.RedisOwnProperties;
import com.cyz.basic.service.support.RedisSupport;

@Service
@EnableConfigurationProperties(RedisOwnProperties.class)
public class RedisService extends RedisSupport {
	
	public enum KeyPrefix {
		
		userInfo,sessionInfo
		
	}
	
    @Autowired
    private RedisOwnProperties ps;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	protected RedisTemplate<String, Object> getRedisTemplate() {		
		return redisTemplate;
	}

	@Override
	protected StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}
	
	public Map<String, Long> getExpires() {
		return ps.getExpireMap();
	}

	public String completeKey(KeyPrefix prefix, String key) {
		
		return prefix.name().concat("::").concat(key);
	}
}
