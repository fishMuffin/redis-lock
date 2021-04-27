package com.example.redislock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {

    @Bean
    public Jedis jedis(){
        Jedis jedis = new Jedis("192.168.1.106", 6379, 10000);
        return jedis;
    }
}