package com.tony.admin.web.common.log;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 统一日志处理
 * @author Guoqing.Lee
 * @date 2019年1月3日 上午11:29:13
 *
 */
@Aspect
@Component
@Order(1)
public class LogAspect {

	private Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	@Pointcut("execution(public * com.tony.admin.web.*.controller.*.*(..))")
	public void logPointCut() {}
	
	/**
	 * 在切点前执行
	 * @param joinPoint
	 */
	@Before("logPointCut()")
	public void doBefore(JoinPoint joinPoint) {
	}

	/**
	 * 在切点后，return前执行
	 * @param joinPoint
	 */
	@Around("logPointCut()")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
		long startTime = System.currentTimeMillis();
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();
		Object result = joinPoint.proceed();
		String url = request.getRequestURL().toString();
		String httpMethod = request.getMethod();
		String ip = getIpAddr(request);
		String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "."+ joinPoint.getSignature().getName();
		String parameters = Arrays.toString(joinPoint.getArgs());
		logger.info("REQUEST URL:" + url + " | HTTP METHOD: " + httpMethod + " | IP: " + ip + " | CLASS_METHOD: " + classMethod
				+ " | ARGS:" + parameters + " | RESPONSE TIME:" + (System.currentTimeMillis() - startTime) + "ms");
		return result;
	}
	
	/**
	 * 在切入点，return后执行，如果相对某些方法的返回参数进行处理，可以在此处执行
	 * @param object
	 */
	@AfterReturning(returning = "object",pointcut = "logPointCut()")
    public void doAfterReturning(Object object){

    }

	/**
	 * 获取真实的IP地址
	 * @param request
	 * @return
	 */
	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
