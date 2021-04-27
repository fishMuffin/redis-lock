package com.example.redislock.temp;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class RedisUtils {

    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate RedisTemplate;

    public void del(String prefix) {
        Set<String> keys = RedisTemplate.keys("user*");
        log.info("keys {} size {}", JSONObject.toJSONString(keys), keys.size());
        RedisTemplate.delete(keys);
    }
}
