package com.chuyx.service.impl;

import com.chuyx.mapper.UserMapper;
import com.chuyx.pojo.dto.LoginUserDTO;
import com.chuyx.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class LoginUserServiceImpl implements LoginUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<GrantedAuthority> getRoles(String role) {
        if (role.equals("ordinary")) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_Ordinary");
        } else if (role.equals("author")) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_Author");
        } else {
            return role.equals("admin") ? AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_Admin") : AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_Ordinary");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LoginUserDTO userInfo = userMapper.queryUserByUsername(username);
        List<GrantedAuthority> roles = new ArrayList();
        if (userInfo.getCapacity() == 0) {
            roles = getRoles("ordinary");
        } else if (userInfo.getCapacity() == 1) {
            roles = getRoles("author");
        } else if (userInfo.getCapacity() == 2) {
            roles = getRoles("admin");
        }

        User user = new User(userInfo.getUname(), userInfo.getPassword(), (Collection) roles);
        return user;
    }
}
