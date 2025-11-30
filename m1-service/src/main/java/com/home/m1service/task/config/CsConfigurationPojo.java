package com.home.m1service.task.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Data
@RefreshScope //needed for Vault (runtime update via actuator)
@ConfigurationProperties("cs.properties")
public class CsConfigurationPojo { //do not use RECORD, as @RefreshScope creates a proxy by extending the class
    private String mName;
    private String mPass;
    private String mType;
    private String m2ServiceUrl;
    private String m3ServiceUrl;

    public String getmPass() {
        return Optional.ofNullable(mPass)
                .map(p -> new String(Base64.getDecoder().decode(p), StandardCharsets.UTF_8)).orElse(StringUtils.EMPTY);
        //return new String(Base64.getDecoder().decode(mPass), StandardCharsets.UTF_8);
    }
}