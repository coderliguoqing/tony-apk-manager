package com.tony.admin.web.common.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tony.admin.web.common.security.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.gson.Gson;
import com.tony.admin.web.common.redis.RedisRepository;
import com.tony.admin.web.common.utils.RandomHelper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

/**
 * jwt token 工具类
 * jwt的claim里一般包含以下几种数据
 * 		1、iss	token的发行者
 * 		2、sub	该JWT所面向的用户
 * 		3、aud	接收该jwt的乙方
 * 		4、exp	token的失效时间
 * 		5、nbf	在此时间之前，不会被处理
 * 		6、iat	jwt的发布时间
 * 		7、jti	jwt的唯一标识，防止重复使用
 * @author Guoqing
 * @Date 2018-01-12
 *
 */
@Component
public abstract class JwtTokenUtil {

    public static final String TOKEN_TYPE_BEARER = "Bearer";

    public static final String USER_AUTH_TOKEN = "_user_auth_token_";

    public static final String USER_AUTH_INFO = "_user_auth_info_";

    @Value("${spring.application.name}")
    private String appName;

    private String header = "Authorization";

    private String secret = "defaultSecret";

    private Long expiration = 604800L;

    private String authPath = "auth";

    private String md5Key = "randomKey";
	
	@Autowired
	private RedisRepository redisRepository;
	
	/**
	 * 从token中获取用户名
	 * @param token
	 * @return
	 */
	public String getUsernameFromToken( String token ){
		return getClaimFromToken(token).getSubject();
	}
	
	/**
	 * 从token获取jwt的发布时间
	 * @param token
	 * @return
	 */
	public Date getIssueAtDateFromToken(String token){
		return getClaimFromToken(token).getIssuedAt();
	}
	
	/**
	 * 从token获取失效时间
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token){
		return getClaimFromToken(token).getExpiration();
	}
	
	/**
	 * 获取jwt的接收者
	 * @param token
	 * @return
	 */
	public String getAudienceFromToken(String token){
		return getClaimFromToken(token).getAudience();
	}
	
	/**
	 * 获取使用的jwt claim
	 * @param token
	 * @param key
	 * @return
	 */
	public String getPrivateClaimFromToken(String token, String key){
		return getClaimFromToken(token).get(key).toString();
	}
	
	/**
	 * 获取md5 key
	 * @param token
	 * @return
	 */
	public String getMd5KeyFromToken(String token){
		return getPrivateClaimFromToken(token, md5Key);
	}

	/**
	 * 获取jwt的payload部分
	 * @param token
	 * @return
	 */
	public Claims getClaimFromToken(String token){
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	/**
	 * 验证token是否过期，true为过期，false为没过期
	 * @param token
	 * @return
	 */
	public Boolean isTokenExpired(String token){
		final Date expriation = getExpirationDateFromToken(token);
		String userName = getUsernameFromToken(token);
		String redisToken = redisRepository.get(appName + USER_AUTH_TOKEN +userName);
		return expriation.before(new Date()) && token.equals(redisToken);
	}
	
	/**
	 * 解析token是否正确，不正确抛出异常
	 * @param token
	 * @throws JwtException
	 */
	public void parseToken(String token) throws JwtException{
		Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}
	
	/**
	 * 生成token（通过用户名和签名时候用的随机数）
	 * @param randomKey
	 * @return
	 */
	public String generateToken(UserDetails userDetails, String randomKey){
		Map<String, Object> claims = new HashMap<>();
		claims.put(md5Key, randomKey);
		String token = doGenerateToken(claims, userDetails);
		//将生成的token存入redis做唯一性校验
		redisRepository.setExpire(appName + USER_AUTH_TOKEN + userDetails.getUsername(), token, expiration);
		redisRepository.setExpire(appName + USER_AUTH_INFO + userDetails.getUsername(), new Gson().toJson(userDetails), expiration );
		return token;
	}
	
	/**
	 * 生成token
	 * @param claims
	 * @return
	 */
	private String doGenerateToken(Map<String, Object> claims, UserDetails userDetails){
		final Date createDate = new Date();
		final Date expirationDate = new Date(createDate.getTime() + expiration*1000);
		
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(createDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	/**
	 * 获取呼啸MD5签名用的随机字符串
	 * @return
	 */
	public String getRandomKey(){
		return RandomHelper.getRandomString(6);
	}
	
	/**
	 * 失效token
	 * @param token
	 */
	public void deleteToken(String token){
		String userName = getUsernameFromToken(token);
		redisRepository.del(appName + USER_AUTH_INFO + userName);
		redisRepository.del(appName + USER_AUTH_INFO + userName);
	}
	
	/**
	 * 获取用户信息
	 * @param token
	 * @return
	 */
	public abstract UserDetails getUserDetails(String token);

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Long getExpiration() {
		return expiration;
	}

	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}

	public String getAuthPath() {
		return authPath;
	}

	public void setAuthPath(String authPath) {
		this.authPath = authPath;
	}

	public String getMd5Key() {
		return md5Key;
	}

	public void setMd5Key(String md5Key) {
		this.md5Key = md5Key;
	}
	
	public static String getTokenTypeBearer() {
		return TOKEN_TYPE_BEARER;
	}

	/**
	 * 验证令牌
	 *
	 * @param token       令牌
	 * @param userDetails 用户
	 * @return 是否有效
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		AuthUser user = (AuthUser) userDetails;
		String username = getUsernameFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
	}
	
}
