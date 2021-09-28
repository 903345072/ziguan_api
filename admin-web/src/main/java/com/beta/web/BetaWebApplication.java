package com.beta.web;

import com.show.api.NormalRequest;
import com.sun.jna.Native;
import com.util.remote;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;


import java.nio.charset.Charset;
import java.text.NumberFormat;

@SpringBootApplication()
@MapperScan("com.mapper")
@ComponentScan(basePackages = {"com.beta.web.schedule","com.beta.web.netty","com.beta","com.util","com.stock","listener", "com.mapper","com.interceptor","com.beta.web.exceptionHandler"})
@EnableTransactionManagement

public class BetaWebApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(BetaWebApplication.class, args);
        NettyServer nettyServer = new NettyServer(1234);
        nettyServer.start();
    }

    @Bean
    NumberFormat numberFormatInstance(){
        return NumberFormat.getInstance();
    }



}


