package com.beta.web.aspect;

import com.util.RetResponse;
import com.util.RetResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Aspect
@Component
public class ExceptionHandlerAspect {


    @Pointcut("execution(public * com.beta.web.controller.*.*(..)) || execution(public * com.beta.web.controller.FrontendApi.*.*(..))")
    public void pointcut(){}

    @Around("pointcut()")
    public RetResult doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        RetResult r = null;
        try {
            r = (RetResult) joinPoint.proceed(joinPoint.getArgs());
        }catch (Exception throwable){
            throwable.printStackTrace();
            return  new RetResult(505,throwable.getMessage(),"");
        }
        return  r;
    }

}