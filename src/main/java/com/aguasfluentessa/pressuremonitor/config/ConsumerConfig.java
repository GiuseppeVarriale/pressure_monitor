package com.aguasfluentessa.pressuremonitor.config;

import com.aguasfluentessa.pressuremonitor.service.RedisQueueConsumer;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.annotation.PostConstruct;

@Configuration
public class ConsumerConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisQueueConsumer.class);
    private final RedisQueueConsumer redisQueueConsumer;

    public ConsumerConfig(RedisQueueConsumer redisQueueConsumer) {
        this.redisQueueConsumer = redisQueueConsumer;
    }

    @PostConstruct
    public void startConsumers() {
        redisQueueConsumer.startConsuming();
        String message = "Redis Consumer Started";
        logger.info(message);
        System.out.println(message);
    }
}