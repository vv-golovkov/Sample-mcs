package com.home.m1service.task.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.reactive.function.client.WebClient;

//@ConfigurationProperties("cs.url")
//@RefreshScope(proxyMode = ScopedProxyMode.INTERFACES)
//public record WebClientConfiguration(String m2service, String m3service) {
public record WebClientConfiguration(WebClientRecord record) implements WebClientConfigurationMarker {
    public WebClientRecord get() {
        return record;
    }

//    @Bean
//    @Qualifier("m2ServiceClient")
//    public WebClient m2ServiceClient(WebClient.Builder builder) {
//        return builder.baseUrl(m2service).build();
//    }
//    public WebClient m2ServiceClient(WebClient.Builder builder) {
//        return builder.baseUrl(get().m2service()).build();
//    }
//
//    @Bean
//    @Qualifier("m3ServiceClient")
//    public WebClient m3ServiceClient(WebClient.Builder builder) {
//        return builder.baseUrl(m3service).build();
//    }
//    public WebClient m3ServiceClient(WebClient.Builder builder) {
//        return builder.baseUrl(get().m3service()).build();
//    }
}
