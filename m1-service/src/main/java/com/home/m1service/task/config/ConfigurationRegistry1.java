package com.home.m1service.task.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration //automatically creates proxy via CGLIB. So, this class can NOT be a record.
@RequiredArgsConstructor
@EnableConfigurationProperties({CsConfigurationPojo.class})
public class ConfigurationRegistry1 {
    private final CsConfigurationPojo csConfigurationPojo;

    @Bean
    //@LoadBalanced - requires 'discovery.enabled=true' (in Consul/K8s)
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        log("CONFIGURATION REFRESH");
    }

    /*
    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log("CONFIGURATION READY");
    }
     */

    private void log(String s) {
        log.info("\n====={}=====\n{}", s, csConfigurationPojo);
    }
}