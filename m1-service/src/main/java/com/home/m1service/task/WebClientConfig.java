package com.home.m1service.task;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private static final String M2_SERVICE_URL = "http://m2service:8080";
    private static final String M3_SERVICE_URL = "http://m3service:8080";

    @Bean
    @Qualifier("m2ServiceClient")
    public WebClient m2ServiceClient(WebClient.Builder builder) {
        return builder.baseUrl(M2_SERVICE_URL).build();
    }

    @Bean
    @Qualifier("m3ServiceClient")
    public WebClient m3ServiceClient(WebClient.Builder builder) {
        return builder.baseUrl(M3_SERVICE_URL).build();
    }
}
