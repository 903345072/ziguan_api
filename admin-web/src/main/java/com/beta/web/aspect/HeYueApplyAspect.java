package com.beta.web.aspect;

import com.beta.web.contextHolder.MemberHolder;
import com.beta.web.exception.InsufficientMoneyExceptioin;
import com.mapper.frontend.MemberMapper;
import com.stock.models.frontend.Member;
import com.stock.service.ServiceImpl.HeYueCaculateAdpterInterface;
import com.util.RetResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Arrays;

@Aspect
@Component
public class HeYueApplyAspect {
    @Autowired
    @Qualifier("HeYueCaculateAdper")
    HeYueCaculateAdpterInterface HeYueCaculateImpl;
    @Autowired
    MemberMapper memberMapper;

    @Pointcut("execution(public * com.beta.web.controller.FrontendApi.HeYueApi.applyHeYue(..))")
    public void pointcut(){}

    @Before("pointcut()")
    public void deBefore(JoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();
        double deposit = Double.parseDouble(request.getParameter("deposit"));
        int heyue_id = Integer.parseInt(request.getParameter("heyue_id"));
        int leverage_id = Integer.parseInt(request.getParameter("leverage_id"));
        double leverage_money = HeYueCaculateImpl.cal_leverage_money(deposit, leverage_id);
        double interest_rate = HeYueCaculateImpl.cal_interest_rate(heyue_id,leverage_id);
        int capitial_used_time = HeYueCaculateImpl.cal_capitial_used_time(heyue_id);
        double interest = HeYueCaculateImpl.cal_interest(interest_rate,leverage_money,capitial_used_time);
        double repare_capitical = HeYueCaculateImpl.cal_repare_capitical(deposit,interest);
        Member member = memberMapper.findMember(MemberHolder.getUsername());
        //查询余额对比准备资金
        if(member.getAmount().doubleValue() < repare_capitical){
             throw new InsufficientMoneyExceptioin("余额不足");
        }
    }

}