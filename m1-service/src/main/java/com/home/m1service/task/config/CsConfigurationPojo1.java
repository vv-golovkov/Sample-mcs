package com.home.m1service.task.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@Data
@RefreshScope //needed for Vault (runtime update via actuator)
@ConfigurationProperties("cs.properties")
public class CsConfigurationPojo1 { //do not use RECORD, as @RefreshScope creates a proxy by extending the class
    private String mName;
    private String mPass;
    private String mType;
    private String m2ServiceUrl;
    private String m3ServiceUrl;

    public void setMPass(String mPass) {
        log.info("M1: Lombok has triggered for mPass-change [old={}; new={}]", this.mPass, mPass);
        this.mPass = Optional.ofNullable(mPass)
                .map(p -> new String(Base64.getDecoder().decode(p), StandardCharsets.UTF_8)).orElse(null);
    }

//    public String getMPassDecoded() {
//        return Optional.ofNullable(mPass)
//                .map(p -> new String(Base64.getDecoder().decode(p), StandardCharsets.UTF_8)).orElse(null);
//        //return new String(Base64.getDecoder().decode(mPass), StandardCharsets.UTF_8);
//    }
}