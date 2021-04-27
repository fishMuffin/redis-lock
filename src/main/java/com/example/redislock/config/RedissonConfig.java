//package com.example.redislock.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RedissonConfig {
//
//    @Bean
//    public RedissonClient getRedisson(){
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://localhost:6379");
//        return Redisson.create(config);
//    }
//}