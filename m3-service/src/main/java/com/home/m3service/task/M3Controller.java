package com.home.m3service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class M3Controller {
    private final String INSTANCE = System.getenv().getOrDefault("HOSTNAME", "unk");
    private final Sender3 sender;

    @GetMapping("/ping")
    public String ping() {
        log.trace("m3:trace");
        log.debug("m3:debug");
        log.info("m3:info");
        log.warn("m3:warn [instance={}]", INSTANCE);
        return "pong3(%s)".formatted(INSTANCE);
    }

    @GetMapping("/call1")
    public ResponseEntity<String> callM1() {
        log.info("M3Controller.callM1()...");
        return sender.callM1Service("/ping");
    }
}
