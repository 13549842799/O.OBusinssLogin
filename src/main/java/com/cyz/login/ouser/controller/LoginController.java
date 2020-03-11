package com.cyz.login.ouser.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyz.basic.constant.EntityConstants;
import com.cyz.basic.net.SessionInfo;
import com.cyz.login.common.pageModel.ResponseResult;
import com.cyz.login.ouser.pojo.LogOutForm;
import com.cyz.login.ouser.pojo.LoginForm;
import com.cyz.login.ouser.pojo.Ouser;
import com.cyz.login.ouser.pojo.WebMessage;
import com.cyz.login.ouser.service.OuserService;
import com.cyz.login.ouser.service.WebMessageService;



/**
 * 单点登录系统
 * 首次登录：验证是否存在此用户，验证失败返回登录页面，验证成功，生成令牌token，返回给客户端。
 * 跳转到数据库中保存的主页位置，如果请求中有携带目标ur了，则跳转到目标url。
 * 本系统的单点登录中各系统拥有独立的过期时间
 * 所有系统共享一个token，然后在各种的系统中保存session
 * 流程：系统a进入主页，（传递username和token）查询本地系统session是否存在或是否过期，有的话本系统已登录
 * @author cyz
 *
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {
	
	@Autowired
	private WebMessageService webMessageService;
	@Autowired
	private OuserService service;

	/**
	    * 职员登陆的接口
	    * 算法：首先获取登陆的网站的对象，获取目标的账号信息：
	    * 1.如果有账号，则判断是否具有登陆此网站的权限，没有权限则失败
	    * 1.1.成功则判断密码是否正确，不正确则返回失败信息。
	    * 1.2.成功则创建token，然后保存sessioninfo（登陆信息）然后返回ok
	    * 2.如果没有账号，则判断是否拥有编号与登陆用户名相同的职员记录，没有则返回失败
	    * 2.1。有则判断此职员是否拥有账号，有则失败
	    * 2.2.没有则创建一个admin账号记录，然后同上
	    * @param request
	    * @param session
	    * @param code
	    * @param userName
	    * @param password
	    * @param mac
	    * @return
	    */
	   @PostMapping("/{type}/loginAsyn.do")
	   public ResponseResult<Map<String,Object>> loginAsyn(HttpServletRequest request,
			    @PathVariable("type") Integer type,
			    @RequestBody LoginForm form){
			ResponseResult<Map<String,Object>>  response = new ResponseResult<>();
			WebMessage web = webMessageService.getByCode(form.getCode());
		    if (web == null) {
		    	return response.fail(ResponseResult.RESPONSE_FAIL_WEBMESSAGE, "不存在对应网站", null);
		    }		    
			Ouser user = service.get(new Ouser(form.getUsername()));
			if (user == null) {
				return response.fail(ResponseResult.RESPONSE_FAIL_USERNAME, "用户名不存在", null);
			}
			if (!service.checkPasswordValid(user.getPassword(), form.getPassword())) {
				return response.fail(ResponseResult.RESPONSE_FAIL_PASSWORD, "密码错误", null);
			}
			Map<String,Object> result = new HashMap<>();
			SessionInfo session = service.saveSessonInfo(user, web, EntityConstants.tokenKey(type, user.getUsername()));
			result.put("session", session);
			return response.success("ok", result);
	   }
	   
	   @PostMapping("/{type}/logout.do")
	   public ResponseResult<Object> logout(HttpServletRequest request,
			    @PathVariable("type") Integer type,
			    @RequestBody LogOutForm form){
			ResponseResult<Object>  response = new ResponseResult<>();	    
			Ouser user = service.get(new Ouser(form.getUsername()));
			if (user == null) {
				return response.fail(ResponseResult.RESPONSE_FAIL_USERNAME, "用户名不存在", null);
			}
			boolean result = service.logOut(user, form.getToken(), EntityConstants.tokenKey(type, user.getUsername()));
			
			return response.success("ok");
	   }
	   
	   @GetMapping("/test.do")
	   public ResponseResult<Ouser> test() {
		   ResponseResult<Ouser>  response = new ResponseResult<>();
		   service.testCache(null, null);
		   return response.success();
	   }

}
