package com.beta.web.filter;

import com.beta.web.service.UserSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.models.User;
import com.stock.models.common.CommonUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 验证用户名密码正确后，生成一个token，放在header里，返回给客户端
 * @author 程就人生
 * @date 2019年5月26日
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {

        this.authenticationManager = authenticationManager;

    }

    /**
     * 接收并解析用户凭证，出現错误时，返回json数据前端
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            CommonUser user =new ObjectMapper().readValue(req.getInputStream(), CommonUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            user.getPassword(),
                            new ArrayList<>())
            );
        }catch (BadCredentialsException | InternalAuthenticationServiceException e){  //用户密码错误异常
            try {
                e.printStackTrace();
                //登录成功時，返回json格式进行提示
                responseJson(res,"用户或密码错误",406);
                return null;
            } catch (Exception e1) {
                 e.printStackTrace();
            }
            throw new BadCredentialsException("用户或密码错误");
        }catch (DisabledException e){
            try {
                //登录成功時，返回json格式进行提示
                responseJson(res,"账户已被禁用",407);
                return null;
            } catch (Exception e1) {
                e.printStackTrace();
            }
            throw new BadCredentialsException("账户已被禁用");
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * 用户登录成功后，生成token,并且返回json数据给前端
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth){

        //json web token构建

                HashMap<String,Object> hm = new HashMap<>();
                hm.put("username",((UserSetter) auth.getPrincipal()).getUsername());
                hm.put("authorities",((UserSetter) auth.getPrincipal()).getAuthorities());
                hm.put("id",((UserSetter) auth.getPrincipal()).getId());

                String token = Jwts.builder()
                //此处为自定义的、实现org.springframework.security.core.userdetails.UserDetails的类，需要和配置中设置的保持一致
                //此处的subject可以用一个用户名，也可以是多个信息的组合，根据需要来定
                .claim("userinfo",hm)


                //设置token过期时间，24小時
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000))

                //设置token签名、密钥
                .signWith(SignatureAlgorithm.HS512, "MyJwtSecret")

                .compact();

        //返回token
        res.addHeader("Authorization", "Bearer " + token);
//        ArrayList<SimpleGrantedAuthority> authorities = (ArrayList) ((UserSetter) auth.getPrincipal()).getAuthorities();
//        UsernamePasswordAuthenticationToken authentication =  new UsernamePasswordAuthenticationToken(hm,null, authorities);
//        //获取后，将Authentication写入SecurityContextHolder中供后序使用
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        try {
            //登录成功時，返回json格式进行提示
            res.setContentType("application/json;charset=utf-8");
            res.setStatus(HttpServletResponse.SC_OK);
            PrintWriter out = res.getWriter();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("code", HttpServletResponse.SC_OK);
            map.put("message","登陆成功！");
            map.put("token","Bearer " + token);
            map.put("data","Bearer " + token);
            out.write(new ObjectMapper().writeValueAsString(map));
            out.flush();
            out.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

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
}
