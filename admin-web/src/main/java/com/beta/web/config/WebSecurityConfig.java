package com.beta.web.config;

import com.beta.web.filter.JWTAuthenticationFilter;
import com.beta.web.filter.JWTLoginFilter;
import com.beta.web.service.Http501ForbiddenEntryPoint;
import com.beta.web.service.MyAccessDeniedHandler;
import com.beta.web.voter.RoleBasedVoter;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭跨站请求防护
                .cors().and().csrf().disable()
                //允许不登陆就可以访问的方法，多个用逗号分隔
                .authorizeRequests()
                .antMatchers("/getConfig","/member/register/forget_password","/member/register","/member/register/senCode","/frontend/getRankList","/stock/optional_list/{list}","/que","/testd","/out","/member/getMemberHeYueApplyList","/stock/getDaPanData","/stock/getRankList/{page}","/login","/stockServer/{userId}/{stockId}","/stock/getTimeSharingData/{stockCode}","/stock/getKdata/{period}/{stock_code}","/stock/getTradeData/{stockCode}").permitAll()
                //其他的需要授权后访问
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                //增加登录拦截
                .addFilter(new JWTLoginFilter(authenticationManager()))
                // 前后端分离是无状态的，所以暫時不用session，將登陆信息保存在token中。
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                // 自定义accessDecisionManager
                .accessDecisionManager(accessDecisionManager())
                .and()
                .exceptionHandling()
                .accessDeniedHandler(getAccessDeniedHandler())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new Http501ForbiddenEntryPoint());
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //覆盖UserDetailsService类
//        auth.userDetailsService(myCustomUserService)
//                //覆盖默认的密码验证类
//                .passwordEncoder(myPasswordEncoder);
//    }

    @Bean
    public PasswordEncoder bcryptEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(),
                // new RoleVoter(),
                new RoleBasedVoter(),
                new AuthenticatedVoter()
        );
        return new UnanimousBased(decisionVoters);
    }
    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }
}
