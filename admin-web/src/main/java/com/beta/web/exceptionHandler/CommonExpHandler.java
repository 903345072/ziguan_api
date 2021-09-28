package com.beta.web.exceptionHandler;

import com.beta.web.exception.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommonExpHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CommonExpHandler.class);
    @ExceptionHandler(value = LoginException.class)
    Object handleException(LoginException e, HttpServletRequest request){
        LOG.error("url {}, msg {}",request.getRequestURL(), e.getMessage());
        Map<String, Object> map = new HashMap<>();
        map.put("code", e.getCode());
        map.put("msg", e.getMessage());
        map.put("url", request.getRequestURL());
        return map;
    }
}
