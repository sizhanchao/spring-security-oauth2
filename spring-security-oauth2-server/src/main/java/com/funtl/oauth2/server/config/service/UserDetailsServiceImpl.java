package com.funtl.oauth2.server.config.service;

import com.funtl.oauth2.domain.TbPermission;
import com.funtl.oauth2.domain.TbUser;
import com.funtl.oauth2.service.TbPermissionService;
import com.funtl.oauth2.service.TbUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhan
 * @since 2019-11-15 10:02
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    TbUserService tbUserService;

    @Autowired
    TbPermissionService tbPermissionService;

    /**
     * 认证和授权
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TbUser user = tbUserService.getByUserName(username);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        if (user != null) {
            List<TbPermission> tbPermissions = tbPermissionService.selectByUserId(user.getId());
            tbPermissions.forEach(tbPermission -> {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(tbPermission.getEnname());
                grantedAuthorities.add(grantedAuthority);
            });
        }
        return new User(user.getUsername(),user.getPassword(),grantedAuthorities);
    }
}
