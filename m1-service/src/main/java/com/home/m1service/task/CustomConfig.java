package com.home.m1service.task;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("cs.some1")
public record CustomConfig(String myParam) {
}
