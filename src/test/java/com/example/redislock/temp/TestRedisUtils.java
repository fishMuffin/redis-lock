package com.example.redislock.temp;

import com.example.redislock.RedisLockApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestRedisUtils extends RedisLockApplicationTests {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void testRedisUtils() {

        redisUtils.del("user");
    }
}
