package com.home.m1service.task;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

//@RefreshScope - needs ONLY for ConfigurationProperties that are directly used in code and can be dynamically changed.
@RefreshScope(proxyMode = ScopedProxyMode.INTERFACES)
@ConfigurationProperties("cs.some1")
public record CustomConfigRecord(String myParam) implements ICustomConfig {
    public String get() {
        return myParam;
    }
}
