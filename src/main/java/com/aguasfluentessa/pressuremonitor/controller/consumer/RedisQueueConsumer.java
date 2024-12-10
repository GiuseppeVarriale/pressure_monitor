package com.aguasfluentessa.pressuremonitor.controller.consumer;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.aguasfluentessa.pressuremonitor.service.PressureGaugeReadingService;
import com.aguasfluentessa.pressuremonitor.service.PressureProcessor;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RedisQueueConsumer {
    private static final Logger logger = LoggerFactory.getLogger(RedisQueueConsumer.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final PressureProcessor pressureProcessor;
    private final ExecutorService executorService;
    private final PressureGaugeReadingService pressureGaugeReadingService;

    public RedisQueueConsumer(RedisTemplate<String, Object> redisTemplate,
            PressureProcessor pressureProcessor,
            PressureGaugeReadingService pressureGaugeReadingService) {
        this.redisTemplate = redisTemplate;
        this.pressureProcessor = pressureProcessor;
        this.executorService = Executors.newFixedThreadPool(10);
        this.pressureGaugeReadingService = pressureGaugeReadingService;
    }

    @PostConstruct
    public void startConsuming() {
        for (int i = 0; i < ((ThreadPoolExecutor) executorService).getCorePoolSize(); i++) {
            executorService.submit(this::consumeQueue);
            logger.info("Started consumer thread {}", i + 1);
        }
    }

    private void consumeQueue() {
        while (true) {
            String gaugeUniqueIdentificator = (String) redisTemplate.opsForList()
                    .rightPop("pressureReadingsProcessQueue");
            if (gaugeUniqueIdentificator != null) {
                List<Double> readings = pressureGaugeReadingService.getLastReadings(gaugeUniqueIdentificator);
                if (readings.size() > 0) {
                    pressureProcessor.processPressure(readings, gaugeUniqueIdentificator);
                }
            }
        }
    }
}