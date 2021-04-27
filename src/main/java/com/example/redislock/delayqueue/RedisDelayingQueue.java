//package com.example.redislock.delayqueue;
//
//import com.alibaba.fastjson.JSONObject;
//import com.alibaba.fastjson.TypeReference;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import redis.clients.jedis.Jedis;
//
//import java.lang.reflect.Type;
//import java.util.Set;
//import java.util.UUID;
//
//@Component
//public class RedisDelayingQueue <T>{
//
//    @Autowired
//    private Jedis jedis;
//
//    /**
//     * 为什么protected访问权限能够被别的包下类访问到
//     */
//    private Type TaskType = new TypeReference<TaskItem<T>>(){}.getType();
//
//    /**
//     * 模拟任务？？？
//     * @param <T>
//     */
//    static class TaskItem<T> {
//        public String id;
//        public T msg;
//    }
//
//    /**
//     * 指定队列，指定消息
//     * 这个id是用作幂等性，防止重复性消费？
//     * @param delayQueue
//     * @param msg
//     */
//    public void delay(String delayQueue, T msg) {
//
//        TaskItem<T> task = new TaskItem<>();
//        task.id = UUID.randomUUID().toString();
//        task.msg = msg;
//        String s = JSONObject.toJSONString(task);
//        jedis.zadd(delayQueue, System.currentTimeMillis() + 5000, s);
//        System.out.println("添加成功");
//    }
//
//    /**
//     * 循环处理消息
//     * @param delayQueue
//     */
//     public void loop(String delayQueue) {
//        long start = System.currentTimeMillis();
//        while (!Thread.interrupted()) {
//
//            Set<String> set = jedis.zrangeByScore(delayQueue, 0, start, 0, 1);
//            if (set.isEmpty()) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    // 这个是break;
//                    e.printStackTrace();
//                    break;
//                }
//                continue;
//            }
//            String value = set.iterator().next();
//            Long zrem = jedis.zrem(delayQueue, value);
//            if (zrem > 0) {
//                TaskItem<T> task = JSONObject.parseObject(value, new TypeReference<TaskItem<T>>() {
//                });
//                handleMsg(task);
//            }
//        }
//    }
//
//    private void handleMsg(TaskItem<T> task) {
//
//        System.out.println("msg" + task.msg);
//    }
//
//    public void main() {
//
//
//        System.out.println("start");
//         Thread producer = new Thread() {
//             public void run() {
//                 System.out.println("生产者生产");
//                 for (int i = 0; i < 10; i++) {
//                     delay("queue", (T) ("codehole" + i));
//                 }
//             }
//         };
//
//         // 多线程情况下会报错
//         Thread consumer = new Thread() {
//             public void run() {
//                 System.out.println("消费者消费");
//                 loop("queue");
//             }
//         };
//         producer.setName("producer");
//         consumer.setName("consumer");
//         producer.start();
//         consumer.start();
//
//
//        // delay("queue", (T) ("codehole"));
//        // loop("queue");
//    }
//
//}
