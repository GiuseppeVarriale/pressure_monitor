package com.aguasfluentessa.pressuremonitor;

import com.aguasfluentessa.pressuremonitor.config.BaseTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PressuremonitorApplicationTests extends BaseTestConfig{
 @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    void testPressureGaugeServiceBean() {
        assertThat(applicationContext.containsBean("pressureGaugeService")).isTrue();
    }

    @Test
    void testPressureGaugeRepositoryBean() {
        assertThat(applicationContext.containsBean("pressureGaugeRepository")).isTrue();
    }
}