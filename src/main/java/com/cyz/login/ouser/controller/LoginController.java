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
 * �����¼ϵͳ
 * �״ε�¼����֤�Ƿ���ڴ��û�����֤ʧ�ܷ��ص�¼ҳ�棬��֤�ɹ�����������token�����ظ��ͻ��ˡ�
 * ��ת�����ݿ��б������ҳλ�ã������������Я��Ŀ��ur�ˣ�����ת��Ŀ��url��
 * ��ϵͳ�ĵ����¼�и�ϵͳӵ�ж����Ĺ���ʱ��
 * ����ϵͳ����һ��token��Ȼ���ڸ��ֵ�ϵͳ�б���session
 * ���̣�ϵͳa������ҳ��������username��token����ѯ����ϵͳsession�Ƿ���ڻ��Ƿ���ڣ��еĻ���ϵͳ�ѵ�¼
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
	    * ְԱ��½�Ľӿ�
	    * �㷨�����Ȼ�ȡ��½����վ�Ķ��󣬻�ȡĿ����˺���Ϣ��
	    * 1.������˺ţ����ж��Ƿ���е�½����վ��Ȩ�ޣ�û��Ȩ����ʧ��
	    * 1.1.�ɹ����ж������Ƿ���ȷ������ȷ�򷵻�ʧ����Ϣ��
	    * 1.2.�ɹ��򴴽�token��Ȼ�󱣴�sessioninfo����½��Ϣ��Ȼ�󷵻�ok
	    * 2.���û���˺ţ����ж��Ƿ�ӵ�б�����½�û�����ͬ��ְԱ��¼��û���򷵻�ʧ��
	    * 2.1�������жϴ�ְԱ�Ƿ�ӵ���˺ţ�����ʧ��
	    * 2.2.û���򴴽�һ��admin�˺ż�¼��Ȼ��ͬ��
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
		    	return response.fail(ResponseResult.RESPONSE_FAIL_WEBMESSAGE, "�����ڶ�Ӧ��վ", null);
		    }		    
			Ouser user = service.get(new Ouser(form.getUsername()));
			if (user == null) {
				return response.fail(ResponseResult.RESPONSE_FAIL_USERNAME, "�û���������", null);
			}
			if (!service.checkPasswordValid(user.getPassword(), form.getPassword())) {
				return response.fail(ResponseResult.RESPONSE_FAIL_PASSWORD, "�������", null);
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
				return response.fail(ResponseResult.RESPONSE_FAIL_USERNAME, "�û���������", null);
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
