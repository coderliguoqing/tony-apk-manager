package com.tony.admin.web.common.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONException;
import com.fasterxml.jackson.core.JsonParseException;
import com.tony.admin.web.common.response.ResponseBean;


/**
 * 全局异常处理
 * @author Guoqing
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(value = AssertException.class)
    @ResponseBody
    public ResponseBean bootExceptionHandler(HttpServletRequest req, AssertException e) {
		ResponseBean response = new ResponseBean(false, e.getCode(), e.getMessage(), null);
    	logger.error(e.getCode()+"",e);
    	logger.warn("ExceptionHandler:"+response.toString());
        return response;
    }

	@ExceptionHandler(value = JSONException.class)
	@ResponseBody
	public ResponseBean jsonExceptionHandler(HttpServletRequest req, JSONException e) {
		ResponseBean response = new ResponseBean(false, 1102, "请求参数格式异常", null);
		logger.error("1102",e);
		logger.error("ExceptionHandler"+response.toString());
		return response;
	}

	@ExceptionHandler(value = JsonParseException.class)
	@ResponseBody
	public ResponseBean jsonParseExceptionHandler(HttpServletRequest req, JsonParseException e) {
		ResponseBean response = new ResponseBean(false, 1102, "请求参数格式异常", null);
		logger.error("1102",e);
		logger.error("ExceptionHandler"+response.toString());
		return response;
	}

	@ExceptionHandler(value = AccessDeniedException.class)
	@ResponseBody
	public ResponseBean accessDefinedExceptionHandler(HttpServletRequest req, AccessDeniedException e){
		logger.error("401", e);
		return ResponseBean.error(401, e.getMessage());
	}

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseBean exceptionHandler(HttpServletRequest req, Exception e) {
		ResponseBean response = new ResponseBean(false, 1000, "服务器正在繁忙，请稍后再试哦~", null);
		logger.error("1000",e);
    	logger.error("ExceptionHandler"+response.toString());
    	return response;
    }

    @ExceptionHandler(value = WebException.class)
    @ResponseBody
    public ResponseBean exceptionHandler(HttpServletRequest req, WebException e) {
		ResponseBean response = new ResponseBean(e.getIsSuccess(), e.getResponseCode(), e.getResponseMsg(), null);
		logger.error(e.getResponseCode()+"",e);
    	logger.error("ExceptionHandler"+response.toString());
    	return response;
    }

}
