package com.tony.admin.web.common.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.cors.CorsUtils;

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
        .authorizeRequests()
        .antMatchers(
			"/auth/token",
			"/auth/getVerifyCode",
			"/auth/getQrcodeContent",
			"/auth/qrcodeCheckLogin",
			"/views/**",
			"/js/**",
			"/images/**"
		)
        .permitAll().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();//设置可跨域请求时的放行
    	security.headers().frameOptions().disable();
    	security
            .csrf().disable()
            .exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint()).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .anyRequest().authenticated();

        // Custom JWT based security filter
        security.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }
}
