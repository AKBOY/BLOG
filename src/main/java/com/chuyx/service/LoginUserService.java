package com.chuyx.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface LoginUserService extends UserDetailsService {
    List<GrantedAuthority> getRoles(String role);
}
