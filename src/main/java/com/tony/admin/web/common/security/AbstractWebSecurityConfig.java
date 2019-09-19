package com.tony.admin.web.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring-security配置
 * @author Guoqing.Lee
 * @date 2019年6月5日 上午10:52:25
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class AbstractWebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 用户信息服务
     */
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;
    @Autowired
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    /**
     * BCryptPasswordEncoder 采用的是SHA-256+随机盐+密码的方式进行加密
     * SHA系列是HASH算法，不是加密算法，过程不可逆，对密码的安全性更好
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
    
    public static void main(String[] args) {
//    	for(int i=0;i<10;i++) {
//    		System.out.println(new BCryptPasswordEncoder(8).encode("123456"));    		
//    	}
    	new BCryptPasswordEncoder(8).matches("123456", "$2a$08$/DkDxcSJRGUWksLUZvbsROf4PUTHur9T50Nn8ZgduVAFd3gqXjviS");
	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Authentication token filter bean authentication token filter.
     *
     * @return the authentication token filter
     */
    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean() {
        return new AuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {
    	security
        .csrf()
        .disable()
        // 基于token，不需要session
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authorizeRequests()
        .antMatchers(
			"/admin/admin/auth/token",
			"/admin/admin/common/getVerifyCode",
			"/admin/admin/auth/getQrcodeContent",
			"/admin/admin/auth/qrcodeCheckLogin",
			"/admin/admin/sys/ueditor/exec",
            "/admin/admin/auth/menu/nav",
            "/admin/admin/auth/user/info",
			"/admin/admin/common/createCloudUploadToken",
			"/admin/admin/im/sendMessage",
			"/views/**",
			"/js/**",
			"/images/**"
		)
        .permitAll()
        .antMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .anyRequest()
        .authenticated()
        // 该设置为了前端在抛异常时可以拿到response
        .and().cors();

        // 添加JWT token的过滤器
        security.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        // 添加自定义未授权和未登录的结果返回
        security.exceptionHandling()
                .accessDeniedHandler(myAccessDeniedHandler)
                .authenticationEntryPoint(myAuthenticationEntryPoint);
    }
}
