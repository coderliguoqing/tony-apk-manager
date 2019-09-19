package com.tony.admin.web.im.listener;

import com.tony.admin.web.common.utils.SpringContextUtil;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Guoqing
 * @desc 订阅消息，redis消息订阅发布是netty集群消息实现的基础
 * @date 2019/8/20
 */
@Component
public class ImMessageListener implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ImMessageListener.class);

    private RedissonClient redissonClient;

    public ImMessageListener(){
        this.redissonClient = (RedissonClient) SpringContextUtil.getBean("redissonCluster");
    }

    @Override
    public void run(String... args) throws Exception {
        RTopic rTopic = redissonClient.getTopic("rTopic");
        rTopic.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence channel, String msg) {
                logger.info("收到topic：{}，Message:{}", channel.toString(), msg);

            }
        });
    }

}
