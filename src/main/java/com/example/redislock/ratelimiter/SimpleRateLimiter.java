package com.example.redislock.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;
// TODO 加一个管道版本

/**
 * 这个是记录一段时间内所有的行为存储起来，如果短时间内请求数量很多，会消耗大量内存空间
 * 因为针对的是一个key ，使用管道可以大量提高redis的存取效率
 */
@Component
@Slf4j
public class SimpleRateLimiter {
    @Autowired
    private Jedis jedis;

    public SimpleRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    // 通过滑动窗口判断在一个窗口时间内是否允许同一个用户一种行为
    //
    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {

        long now = System.currentTimeMillis();
        String key = String.format("%s, %s", userId, actionKey);
        jedis.zadd(key, now, now + "");
        // 删除窗口外的数据
        jedis.zremrangeByScore(key, 0, now - period * 1000);
        // 别忘了加过期时间，避免冷用户持续占用内存
        Long zcard = jedis.zcard(key);
        jedis.expire(key, period + 1);
        log.info("zcard" + zcard);
        return zcard <= maxCount;
    }

    public void main() {
        SimpleRateLimiter limiter = new SimpleRateLimiter(jedis);
        for(int i=0;i<20;i++) {
            System.out.println(limiter.isActionAllowed("laoqian", "reply", 60, 5));
        }
    }
}