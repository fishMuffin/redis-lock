package com.example.redislock.ratelimiter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
/**
 * 这个相比于时间窗口的好处是，不用对每次的请求缓存起来 + 1, 只用判断一下剩余漏斗空间够不够就可以了
 *
 */
public class FunnelRateLimiter {

    public Map<String, Funnel> funnelMap = new HashMap<String, Funnel>();

    static class Funnel {
        // 漏斗容量
        int capacity;
        // 漏水速率
        float leakingRate;
        // 剩余空间
        int leftQuota;
        // 上次漏水时间
        long leakingTs;

        public Funnel(int capacity, float leakingRate) {
            this.capacity = capacity;
            this.leakingRate = leakingRate;
            this.leftQuota = capacity;
            this.leakingTs = System.currentTimeMillis();
        }
    }



    public void makeSpace(Funnel funnel) {

        long nowTs = System.currentTimeMillis();
        // float 可以强壮成int 为什么不可以强转成long
        // 因为float是 4位， int也是 4位
        // double 失去精度也是可以强转成long的
        // long castLong = (long) 1.0;
        long detaTime = nowTs - funnel.leakingTs;
        log.info("nowTs {}, lastTs {} ,时间差 {}", nowTs, funnel.leakingTs, detaTime);
        int detaQuota = (int) (funnel.leakingRate * (detaTime / 1000));
        log.info("腾出空间 {}", detaQuota);
        if (detaQuota < 1) {
            // 如果没有腾出1的空间，下次再来吧
            return;
        }
        // 别忘了设置一下上次的漏水时间
        log.info("已经腾出空间");
        funnel.leakingTs = nowTs;
        log.info("开始更改上次漏水时间");
        // 别忘了如果间隔时间过长，会导致整数溢出
        if (detaQuota < 0) {
            funnel.leftQuota = funnel.capacity;
            return;
        }
        funnel.leftQuota += detaQuota;
        if (funnel.leftQuota > funnel.capacity) {
            funnel.leftQuota = funnel.capacity;
        }
    }


    // 用那个漏斗进行限制
    public boolean isActionAllow(String userId, String actionKey, int count, int capacity, float leakingRate) {

        String funnelName = String.format("%s%s", userId, actionKey);
        Funnel funnel = funnelMap.get(funnelName);
        if (ObjectUtils.isEmpty(funnel)) {
            log.info("初始化一次");
            funnel = new Funnel(capacity, leakingRate);
            funnelMap.put(funnelName, funnel);
        }

        return watering(funnel, count);
    }

    private boolean watering(Funnel funnel, int count) {
        // 先腾出空间
        makeSpace(funnel);
        // 判断空间够不够
        if (funnel.leftQuota < count) {
            return false;
        }
        funnel.leftQuota -= count;
        return true;
    }

    public void main() throws InterruptedException {

        for (int i = 0; i < 20; i++) {
            log.info("isActionAllow {}",isActionAllow("user1", "reply", 1, 9, 1f));
            Thread.sleep(100);
        }
    }
}
