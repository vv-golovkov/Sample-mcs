package com.home.m1service.task;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@ConfigurationProperties("cs.url")
public record WebClientConfig(String m2service, String m3service) {
    //    private static final String M2_SERVICE_URL = "http://m2service:8080";
    //    private static final String M3_SERVICE_URL = "http://m3service:8080";

    @Bean
    @Qualifier("m2ServiceClient")
    public WebClient m2ServiceClient(WebClient.Builder builder) {
        return builder.baseUrl(m2service).build();
    }

    @Bean
    @Qualifier("m3ServiceClient")
    public WebClient m3ServiceClient(WebClient.Builder builder) {
        return builder.baseUrl(m3service).build();
    }
}
