package com.beta.web.filter;

import com.beta.web.exception.InsufficientMoneyExceptioin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.AbstractConfigAttributeRequestMatcherRegistry;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.NestedServletException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 是否登陆验证方法
 * @author 程就人生
 * @date 2019年5月26日
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 對請求進行過濾
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {

        try {


            String header = request.getHeader("Authorization");
            if(header == null || header.equals("") || header.equals("null")){
                chain.doFilter(request, response);
            }else{
                UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
                //获取后，将Authentication写入SecurityContextHolder中供后序使用
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }
        }catch (MalformedJwtException o){
            o.printStackTrace();
            try {
                responseJson(response,"请登录",506);
            } catch (Exception e1) {
                o.printStackTrace();
            }
        }catch(ExpiredJwtException e2 ){
            e2.printStackTrace();
            try {
                responseJson(response,"已过期",501);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }catch (NestedServletException o){   //自定义异常
            o.printStackTrace();
            if(o.getCause() instanceof InsufficientMoneyExceptioin){
                Throwable cause = o.getCause();
                try {
                    responseJson(response,cause.getMessage(),507);
                } catch (Exception e5) {
                    o.printStackTrace();
                }
            }
        }catch(SignatureException kk){
            try {
                responseJson(response,"已过期",501);
            } catch (Exception ss) {
                ss.printStackTrace();
            }
        }catch (Exception e) {
            try {
                responseJson(response,"服务器内部错误",500);
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * 抛出异常
     * @param response
     */
    private void responseJson(HttpServletResponse response, String msg, int code) throws IOException {
        //未登錄時，使用json進行提示
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("code", code);
        map.put("message",msg);
        out.write(new ObjectMapper().writeValueAsString(map));
        out.flush();
        out.close();
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && !token.equals("")) {
            //通过token解析出用户信息
            Claims userinfo =  Jwts.parser().setSigningKey("MyJwtSecret")
                    .parseClaimsJws(token.replace("Bearer ", "")).getBody();
            ArrayList<HashMap> authorities = (ArrayList) ((HashMap) userinfo.get("userinfo")).get("authorities");
            String username = (String)((HashMap) userinfo.get("userinfo")).get("username");
            ArrayList<GrantedAuthority> authorities_ = new ArrayList<>();
            if(authorities!= null && authorities.size()>0){
                for (HashMap v: authorities) {
                    authorities_.add(new SimpleGrantedAuthority((String) v.get("authority")));
                }
            }

            if (username != null) {
                return new UsernamePasswordAuthenticationToken(userinfo.get("userinfo"), null, authorities_);
            }
            return null;
        }
        return null;
    }

}
