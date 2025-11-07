package com.home.m1service.task;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration //automatically creates proxy via CGLIB. So ConfigurationRegistry.class can NOT be a record.
@RequiredArgsConstructor
@EnableConfigurationProperties({WebClientConfig.class, CustomConfig.class})
public class ConfigurationRegistry {
    private final WebClientConfig webClientConfig;
    private final IConfig<String> customConfig;
    //private final CustomConfig customConfig;

    @PostConstruct
    public void onStartup() {
        log.info("========== CONFIGURATION STARTUP ==========");
        log();
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        log.info("========== CONFIGURATION REFRESH ==========");
        log();
    }

    private void log() {
        log.info("{}", webClientConfig);
        log.info("{}", customConfig);
        log.info("===========================================");
    }
}