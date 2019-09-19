package com.tony.admin.web.common.security;

import cn.hutool.json.JSONUtil;
import com.tony.admin.web.common.response.ResponseConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The type My authentication entry point.
 *
 * @author GuoqingLee
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static Logger logger = LoggerFactory.getLogger(MyAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.error("【401】 the token is ivalid!");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(JSONUtil.parse(ResponseConstant.IVALID_TOKEN).toString());
        response.getWriter().flush();
    }
}