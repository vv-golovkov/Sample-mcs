package com.home.m1service.task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

//@RefreshScope - needs ONLY for ConfigurationProperties that are directly used in code and can be dynamically changed.
//@ConfigurationProperties("cs.some1")
//@RefreshScope(proxyMode = ScopedProxyMode.INTERFACES)
public record CustomConfiguration(String myParam) implements CustomConfigurationMarker {
    public String get() {
        return myParam;
    }
}
