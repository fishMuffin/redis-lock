package com.example.redislock.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

@Slf4j
@Component
public class RedisDistributedLock {
    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    // 锁的超时时间
    private static int EXPIRE_TIME = 5 * 1000;
    // 锁等待时间
    private static int WAIT_TIME = 1 * 1000;

    @Autowired
    private Jedis jedis;
    private String key;

    public RedisDistributedLock() {
    }

    // 不断尝试加锁
    public String lock(String key) {
        try {
            // 超过等待时间，加锁失败
            long waitEnd = System.currentTimeMillis() + WAIT_TIME;
            String value = UUID.randomUUID().toString() + Thread.currentThread().getId();
            while (System.currentTimeMillis() < waitEnd) {
                String result = jedis.set(key, value, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, EXPIRE_TIME);
                if (LOCK_SUCCESS.equals(result)) {
                    return value;
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception ex) {
            log.error("lock error", ex);
        }
        return null;
    }

    public boolean release(String key, String value) {
        if (value == null) {
            return false;
        }
        // 判断key存在并且删除key必须是一个原子操作
        // 且谁拥有锁，谁释放
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = new Object();
        try {
            result = jedis.eval(script, Collections.singletonList(key),
                    Collections.singletonList(value));
            if (RELEASE_SUCCESS.equals(result)) {
                log.info("release lock success, value:{}", value);
                return true;
            }
        } catch (Exception e) {
            log.error("release lock error", e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        log.info("release lock failed, value:{}, result:{}", value, result);
        return false;
    }
}