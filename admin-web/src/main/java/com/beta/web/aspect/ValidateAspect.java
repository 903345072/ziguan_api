package com.beta.web.aspect;

import com.beta.web.contextHolder.MemberHolder;
import com.beta.web.exception.InsufficientMoneyExceptioin;
import com.mapper.frontend.MemberMapper;
import com.stock.models.frontend.Member;
import com.stock.service.ServiceImpl.HeYueCaculateAdpterInterface;
import com.util.RetResponse;
import com.util.RetResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Aspect
@Component
public class ValidateAspect {


    @Pointcut("execution(public * com.beta.web.controller.FrontendApi.Order.*(..)) || execution(public * com.beta.web.controller.FrontendApi.*.*(..))")
    public void pointcut(){}

    @Around("pointcut() &&args(..,bindingResult)")
    public RetResult doAround(ProceedingJoinPoint joinPoint, BindingResult bindingResult) throws Throwable {
        if(bindingResult.hasErrors()){

            List<ObjectError> ls=bindingResult.getAllErrors();
            ObjectError error_ = ls.get(0);
            String err = error_.getDefaultMessage();
            return RetResponse.makeErRsp(err);
        }else {
            return (RetResult) joinPoint.proceed(joinPoint.getArgs());
        }
    }

}