package com.home.m1service.task;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration //automatically creates proxy via CGLIB. So ConfigurationRegistry.class can NOT be a record.
@AllArgsConstructor
@EnableConfigurationProperties({WebClientConfig.class, CustomConfig.class})
public class ConfigurationRegistry {
    private final WebClientConfig webClientConfig;
    private final CustomConfig customConfig;

    @PostConstruct
    public void logConfigs() {
        log.info("========================================");
        log.info("{}", webClientConfig);
        log.info("{}", customConfig);
        log.info("========================================");
    }
}