package com.cyz.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * �����¼ϵͳ
 * �����˵�¼У��
 * ��ע��
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
