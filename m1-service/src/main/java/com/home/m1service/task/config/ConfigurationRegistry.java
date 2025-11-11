package com.home.m1service.task.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration //automatically creates proxy via CGLIB. So, this class can NOT be a record.
@RequiredArgsConstructor
@EnableConfigurationProperties({CsConfigurationPojo.class})
public class ConfigurationRegistry {
    private final CsConfigurationPojo csConfigurationPojo;

    @Bean
    @RefreshScope //recreate WebClient as it uses updatable POJO
    @Qualifier("m2ServiceClient")
    public WebClient m2ServiceClient(WebClient.Builder builder) {
        return builder.baseUrl(csConfigurationPojo.getM2ServiceUrl()).build();
    }

    @Bean
    @RefreshScope //recreate WebClient as it uses updatable POJO
    @Qualifier("m3ServiceClient")
    public WebClient m3ServiceClient(WebClient.Builder builder) {
        return builder.baseUrl(csConfigurationPojo.getM3ServiceUrl()).build();
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
        log.info("\n====={}=====\n{}", s, csConfigurationPojo);
    }
}