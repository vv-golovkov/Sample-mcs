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

    public ResponseEntity<String> callM1Service() {
        try {
            log.debug("pojo3={}", pojo);
            return rest.getForEntity(pojo.getM1ServiceUrl() + "/hi", String.class);
        } catch (Exception e) {
            log.error("ExceptionDuringCall", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(".%s[%s]".formatted(e.getClass().getSimpleName(), e.getMessage()));
        }
    }
}
