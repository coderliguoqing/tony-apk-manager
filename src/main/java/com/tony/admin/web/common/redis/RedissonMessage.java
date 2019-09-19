package com.tony.admin.web.common.redis;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Guoqing
 * @desc 基于redisson的消息订阅发布
 * @date 2019/8/20
 */
@Component
public class RedissonMessage {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 发送消息到指定topic
     * @param topic
     * @param message
     */
    public void sendMessage(String topic, Object message){
        RTopic rTopic = redissonClient.getTopic(topic);
        rTopic.publish(message);
    }

    /**
     * 异步发送消息到指定topic
     * @param topic
     * @param message
     */
    public void sendMessageAsync(String topic, Object message){
        RTopic rTopic = redissonClient.getTopic(topic);
        rTopic.publishAsync(message);
    }

}
