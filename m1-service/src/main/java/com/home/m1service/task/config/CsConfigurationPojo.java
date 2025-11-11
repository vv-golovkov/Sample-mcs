package com.home.m1service.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope
@ConfigurationProperties("cs.properties")
public class CsConfigurationPojo {
    private String mName;
    private String m2ServiceUrl;
    private String m3ServiceUrl;
}