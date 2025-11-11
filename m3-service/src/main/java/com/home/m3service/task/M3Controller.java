package com.home.m3service.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class M3Controller {

    @SneakyThrows
    @GetMapping("/ping")
    public String ping() {
        log.trace("m3:trace");
        log.debug("m3:debug");
        log.info("m3:info");
        log.warn("m3:warn");

        log.info("Controller3.ping ...");
        Thread.sleep(3000L);
        log.info("Controller3 pong");
        return "pong3";
    }
}
