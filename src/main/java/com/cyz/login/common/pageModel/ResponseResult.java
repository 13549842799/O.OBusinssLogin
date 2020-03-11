package com.cyz.login.common.pageModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

public class ResponseResult<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4185740114021603198L;
	/**
	 * �ɹ�
	 */
	public static final int RESPONSE_SUCCESS =200;
	/**
	 * ʧ��
	 */
	public static final int RESPONSE_FAIL =100;
	
	/**
	 * �������û���
	 */
	public static final int RESPONSE_FAIL_USERNAME =101;
	/**
	 * �������
	 */
	public static final int RESPONSE_FAIL_PASSWORD =102;
	/**
	 * �����ڶ�Ӧ��վ
	 */
	public static final int RESPONSE_FAIL_WEBMESSAGE =103;
	/**
	 * �쳣
	 */
	public static final int RESPONSE_ERROR=300;
	/**
	 * û�е�¼
	 */
	public static final int RESPONSE_RELOGIN=400;
	
	private int status;// 200 成功   100  失败   300异常  400
	
	private int status_;
	
	private String message;
	
	private Map<String, List<String>> messages;
	
	private T data;
	
	
	public ResponseResult() {
		super();
	}
	
	public ResponseResult(int status, String message) {
		this();
		this.status = status;
		this.message = message;
	}
	
	public ResponseResult(int status, String message, T data) {
		this(status, message);
		this.data = data;
	}
	
	/**
	 * 通过response返回json结果
	 * @param response
	 * @throws IOException
	 */
	public void responseMessage(HttpServletResponse response) throws IOException {
	    response.setContentType("application/json");
		PrintWriter out = response.getWriter();
	    out.println(JSONObject.toJSON(this));
	}
	
	public void responseFailMessage(HttpServletResponse response,String message) throws IOException {
		this.status = RESPONSE_FAIL;
		this.message = message;
		responseMessage(response);
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Map<String, List<String>> getMessages() {
		return messages;
	}
	public void setMessages(Map<String, List<String>> messages) {
		this.messages = messages;
	}
	private ResponseResult<T> setValue(int status, String message, T data) {
		this.message = message;
		this.data = data;
		this.status = status;
		return this;
	}
	
	
	
	public int getStatus_() {
		return status_;
	}

	public void setStatus_(int status_) {
		this.status_ = status_;
	}

	public  ResponseResult<T> success(String mess,T t){
		return setValue(RESPONSE_SUCCESS, mess, t);
	}
	
	public ResponseResult<T> success(){
		return success(null,null);
	}
	
	public ResponseResult<T> success(T t){
		return success(null,t);
	}
	
	public  ResponseResult<T> fail(int status_, String mess,T t){
		this.status_ = status_;
		return setValue(RESPONSE_FAIL, mess, t);
	}
	
	public ResponseResult<T> fail(String mess){
		return fail(0,mess,null);
	}
	
	public  ResponseResult<T> error(String mess,T t){
		return setValue(RESPONSE_ERROR, mess, t);
	}
	
	public  ResponseResult<T> error(String mess){
		return error(mess,null);
	}
	
	/**
	 * 重新登录提示的返回信�?
	 * @return
	 */
	public ResponseResult<T> relogin() {
		return setValue(RESPONSE_RELOGIN, "登录已过�?, 请重新登�?", null);
	}
	
	public ResponseResult<T> relogin(String message) {
		return setValue(RESPONSE_RELOGIN,StringUtils.isEmpty(message) ? "登录已过�?, 请重新登�?" : message, null);
	}
	
	/**
	 * 执行更新操作后返回前端的制式信息
	 * @param result
	 * @return
	 */
	public ResponseResult<T> updateResult(int result) {
		switch (result) {
		case 0:
			return fail("更新失败，当前更新数�?0");
		case 1:
			return success();
		default:
			return error("更新失败，当前更新涉及多条数�?");
		}
	}
	
	/**
	 * 执行删除操作后返回前端的制式信息
	 * @param result
	 * @return
	 */
	public ResponseResult<T> deleteResult(boolean result) {
		return result ? success() : fail("删除失败");
	}
	
	

}
