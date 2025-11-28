package com.home.m1service.task.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@RefreshScope //needed for Vault (runtime update via actuator)
@ConfigurationProperties("cs.properties")
public class CsConfigurationPojo { //do not use RECORD, as @RefreshScope creates a proxy by extending the class
    private String mName;
    private String mPass;
    private String mType;
    private String m2ServiceUrl;
    private String m3ServiceUrl;
}