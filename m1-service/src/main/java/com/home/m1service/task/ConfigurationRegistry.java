package com.home.m1service.task;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Getter
@Configuration //automatically creates proxy via CGLIB. So, this class can NOT be a record.
//@RequiredArgsConstructor(onConstructor_ = {@Qualifier})
@EnableConfigurationProperties({WebClientConfig.class, CustomConfigRecord.class})
public class ConfigurationRegistry {
    private final WebClientConfig webClientConfig;
    private final ICustomConfig customConfig;

    public ConfigurationRegistry(WebClientConfig webClientConfig,
                                 ICustomConfig customConfig) {
        this.webClientConfig = webClientConfig;
        this.customConfig = customConfig;
    }

    @PostConstruct
    public void onStartup() {
        log("CONFIGURATION STARTUP");
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        log("CONFIGURATION REFRESH");
    }

    private void log(String s) {
        log.info("\n====={}=====\n{}\n{}", s, webClientConfig, customConfig);
    }
}