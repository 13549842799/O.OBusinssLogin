package com.cyz.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 单点登录系统
 * 包含了登录校验
 * 简单注册
 * @author cyz
 *
 */
@SpringBootApplication
@EnableCaching
public class Application {
    
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
