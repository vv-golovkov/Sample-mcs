package com.home.m1service.task;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ScopedProxyMode;

@RefreshScope(proxyMode = ScopedProxyMode.INTERFACES)
@ConfigurationProperties("cs.some1")
public record CustomConfig(String myParam) implements IConfig<String> {
    public String get() {
        return myParam;
    }
}
