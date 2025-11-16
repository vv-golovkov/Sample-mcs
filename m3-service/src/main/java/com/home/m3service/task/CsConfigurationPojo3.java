package com.home.m3service.task;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
//@RefreshScope
@ConfigurationProperties("cs.properties")
public class CsConfigurationPojo3 {
    private String mName;
    private String m1ServiceUrl;
    private String m2ServiceUrl;
}
