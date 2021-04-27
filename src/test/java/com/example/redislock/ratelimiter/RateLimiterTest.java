package com.example.redislock.ratelimiter;


import com.example.redislock.RedisLockApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class RateLimiterTest extends RedisLockApplicationTests {

    @Autowired
    private SimpleRateLimiter rateLimiter;

    @Autowired
    private FunnelRateLimiter funnelRateLimiter;

    @Test
    public void testRateLimiter() {

        rateLimiter.main();
    }

    @Test
    public void testFunnelRateLimiter() throws InterruptedException {

        funnelRateLimiter.main();
    }
}
