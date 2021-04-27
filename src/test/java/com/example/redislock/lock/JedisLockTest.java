package com.example.redislock.lock;


import com.example.redislock.RedisLockApplicationTests;
import com.example.redislock.lock.RedisDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

@Slf4j
public class JedisLockTest extends RedisLockApplicationTests {

    @Autowired
    RedisDistributedLock jedisLock;

    @Test
    public void lockTest() {

        String value = jedisLock.lock("ycw");
        log.info("jedis上锁 {}", value);
        String nullValue = jedisLock.lock("ycw");
        log.info("jedis上锁 {}", nullValue);
        jedisLock.release("ycw", value);
        String moreLock = jedisLock.lock("ycw");
        log.info("jedis上锁 {}", moreLock);
    }
}
