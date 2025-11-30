package com.home.m3service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class Sender3 {
    private final RestTemplate rest;
    private final CsConfigurationPojo3 pojo;

    public ResponseEntity<String> callM1Service(String endpoint, Object... vars) {
        String url = pojo.getM1ServiceUrl();
        try {
            log.info("POJO_3={}", pojo);
            if (url == null) {
                return ResponseEntity.ok("M1 URL is null");
            }
            return rest.getForEntity(url + endpoint, String.class, vars);
        } catch (Exception e) {
            log.error("ExceptionDuringCall", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(".%s[%s]".formatted(e.getClass().getSimpleName(), e.getMessage()));
        }
    }
}
