package com.tony.admin.web.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Method;
import java.util.Date;


/**
 *
 * @Description  WEB工具类
 *
 * @author Guoqing
 * @Date 2018年1月16日
 */
public final class WebUtils {

	@SuppressWarnings("unchecked")
	public static <T extends UserDetails> T getCurrentUser(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (T)authentication.getPrincipal();
	}

	/**
	 *
	 * @param obj : 对象bean
	 * @param userCode ：编码
	 * @param userName：姓名
	 * @param setCreate ：是否设值创建人信息
	 * @author liuYang
	 */
	public static void setCreateAndOpInfo(Object obj , String userCode , String userName , Boolean setCreate){
		try {
			if( obj == null){
				return;
			}
			Method method  = null;
			Class<?> aClass = obj.getClass();
			if(setCreate){
				method = aClass.getMethod("setCreateCode",String.class);
				method.invoke(obj , userCode);
				method = aClass.getMethod("setCreateName",String.class);
				method.invoke(obj , userName);
				method = aClass.getMethod("setCreateTime",Date.class);
				method.invoke(obj , new Date());
			}
			method = aClass.getMethod("setOpCode",String.class);
			method.invoke(obj , userCode);
			method = aClass.getMethod("setOpName",String.class);
			method.invoke(obj , userName);
			method = aClass.getMethod("setOpTime",Date.class);
			method.invoke(obj , new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
