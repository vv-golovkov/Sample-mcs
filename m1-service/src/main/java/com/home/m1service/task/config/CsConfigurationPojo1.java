package com.home.m1service.task.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Data
@RefreshScope //needed for Vault/K8s (runtime update via actuator)
@ConfigurationProperties("cs.properties")
public class CsConfigurationPojo1 { //do not use RECORD, as @RefreshScope creates a proxy by extending the class
    private String mName;
    private String mPass; //Spring automatically decodes string when determines Base64-encoding.
    private String mType;
    private String m2ServiceUrl;
    private String m3ServiceUrl;

    /* -> option-1:
    public void setMPass(String mPass) {
        log.info("M1.setMPass: Lombok has triggered for mPass-change [old={}; new={}]", this.mPass, mPass);
        if (mPass != null) {
            this.mPass = new String(Base64.getDecoder().decode(mPass), StandardCharsets.UTF_8);
        }
    }
    */

    //option-2: triggered only once (on startup). The rest time, @RefreshScope uses ConfigurationRegistry1.onRefresh()
    //May be it makes sense to move it to ConfigurationRegistry1 class and leave this DTO clear.
    @PostConstruct
    private void decodeBase64() {
        log.info("M1.decodeBase64: @PostConstruct [{}]", mPass);
        if (mPass != null) {
            mPass = new String(Base64.getDecoder().decode(mPass), StandardCharsets.UTF_8);
        }
        // General option: create annotation @Base64, mark needed fields, decode using reflection (getDeclaredFields)
    }
}