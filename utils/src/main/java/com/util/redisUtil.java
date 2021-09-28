package com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class redisUtil {
    @Autowired
    private RedisTemplate redisTemplate_;
    public void setString(){
        redisTemplate_.opsForValue().set("z","123");
    }
}
