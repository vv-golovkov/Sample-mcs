package com.home.m3service.task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Data
@RefreshScope
@ConfigurationProperties("cs.properties")
public class CsConfigurationPojo3 {
    private String mName;
    private String mPass;
    private String mCategory;
    private String m1ServiceUrl;
    private String m2ServiceUrl;

    public void setMPass(String mPass) {
        log.info("M3: Lombok has triggered for mPass-change [old={}; new={}]", this.mPass, mPass);
        this.mPass = Optional.ofNullable(mPass)
                .map(p -> new String(Base64.getDecoder().decode(p), StandardCharsets.UTF_8)).orElse(null);
    }
}
