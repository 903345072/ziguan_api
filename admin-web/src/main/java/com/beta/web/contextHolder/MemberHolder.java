package com.beta.web.contextHolder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Map;

public class MemberHolder {


    public static Authentication getCurrentUserAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前用户
     * @return
     */
    public static Object getCurrentPrincipal(){
        return getCurrentUserAuthentication().getPrincipal();
    }

    public static int getId(){
        Map currentPrincipal = (Map) getCurrentPrincipal();
        return (int) currentPrincipal.get("id");
    }

    public static String getUsername(){
        Map currentPrincipal = (Map) getCurrentPrincipal();
        return (String) currentPrincipal.get("username");
    }


}
