//package com.example.redislock.hyperloglog;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.Jedis;
//
//@Component
//public class HyperLogLog {
//
//    @Autowired
//    private Jedis jedis;
//
//    public void main() {
//
//        for (int i = 0; i < 100000; i++) {
//
//            jedis.pfadd("codehole", "user" + i);
//            System.out.println("i = " + i);
//        }
//        long total = jedis.pfcount("codehole");
//        System.out.println("total =  " + total + " total/10000 = " +  total / 10000);
//        jedis.close();
//    }
//
//}
