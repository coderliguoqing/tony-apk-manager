package com.tony.admin.web.common.security;

import com.google.gson.Gson;
import com.tony.admin.web.common.redis.RedisRepository;
import com.tony.admin.web.common.security.model.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * The type Token util.
 *
 * @author guoqing
 */
@Component
@ConfigurationProperties("jwt")
public class TokenUtil extends JwtTokenUtil {
	
	@Autowired
	private RedisRepository redisRepository;
	@Value("${spring.application.name}")
	private String appName;
	
	/**
	 * 获取用户信息
	 * @param token
	 * @return
	 */
	@Override
	public UserDetails getUserDetails(String token){
		String userName = getUsernameFromToken(token);
		String user = redisRepository.get(appName + JwtTokenUtil.USER_AUTH_INFO + userName);
		return new Gson().fromJson(user, AuthUser.class);
	}

}