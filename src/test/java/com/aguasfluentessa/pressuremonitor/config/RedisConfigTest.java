package com.aguasfluentessa.pressuremonitor.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

@SpringBootTest
@ActiveProfiles("test")
public class RedisConfigTest extends BaseTestConfig {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    
    private RedisConnectionFactory connectionFactory;

    @BeforeEach
    public void setUp() {
        connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory(), "RedisConnectionFactory must not be null");
        connectionFactory.getConnection().serverCommands().flushDb();
    }

    @AfterEach
    public void tearDown() {
        connectionFactory.getConnection().serverCommands().flushDb();
    }

    @Test
    public void testRedisTemplate() {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set("testKey", "testValue");
        String value = (String) valueOps.get("testKey");
        assertThat(value).isEqualTo("testValue");
    }
}