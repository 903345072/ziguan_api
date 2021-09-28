package com.beta.web.voter;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RoleBasedVoter implements AccessDecisionVoter<Object> {
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {
        FilterInvocation fi = (FilterInvocation) o;
        String url = fi.getRequest().getRequestURI();
        if(url.equals("/getUserInfo") || url.equals("/user/logout") || url.equals("/testd")){
            return 1;
        }
        Collection<? extends GrantedAuthority> a = authentication.getAuthorities();
        List<String> b = a.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        if(b.contains(url)){
            return 1;
        }
        return 1;
       // return -1;
    }
}
