package com.example.redislock.controller;

import com.example.redislock.ratelimiter.FunnelRateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 题目一：
 * 设计一个限流算法，提供 10 qps 的限流能力。
 * 要求：
 * 1. 提供一个 http 接口，正常调用返回 success，限流情况下 返回 throttle
 * 2. 代码提交到 github
 * 3. 可运行
 **/
@RestController
@RequestMapping("/funnel")
public class LimitController {
    @Autowired
    private FunnelRateLimiter funnelRateLimiter;
    @RequestMapping("/limit")
    public Boolean limit(@RequestParam("userId") String userId, @RequestParam("actionKey") String actionKey) {

        return funnelRateLimiter.isActionAllow(userId, actionKey, 1, 10, 0.9f);
    }
}    
    
    