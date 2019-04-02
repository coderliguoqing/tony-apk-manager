package com.tony.admin.web.common.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tony.admin.web.common.security.model.AuthUserFactory;
import com.tony.admin.web.model.SysUser;
import com.tony.admin.web.service.ISystemService;

/**
 * 用户通过用户名密码登录时的实现接口，用户获取用户信息
 *
 * @author Guoqing
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /**
     * 系统服务
     */
    @Autowired
    private ISystemService systemService;

    @Override
    public UserDetails loadUserByUsername(String loginName) {
        SysUser user = systemService.getUserByLoginName(loginName);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", loginName));
        } else {
            return AuthUserFactory.create(user);
        }
    }

}
