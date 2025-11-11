package com.home.m2service.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class M2Controller {

    @SneakyThrows
    @GetMapping("/ping")
    public String ping() {
        log.trace("m2:trace");
        log.debug("m2:debug");
        log.info("m2:info");
        log.warn("m2:warn");

        log.info("Controller2.ping ...");
        Thread.sleep(2000L);
        log.info("Controller2 pong");
        return "pong2";
    }

    @SneakyThrows
    @GetMapping("/rnd/{p}")
    public ResponseEntity<String> rnd(String p) {
        return switch (p) {
            case "un" -> throw new IllegalStateException("Simulated-m2-RuntimeException");
            case "ch" -> throw new IOException("Simulated-m2-IOException");
            //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Simulated 401 error");
            //return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Simulated-m2-UNAUTHORIZED"));
            //return new ResponseEntity<>("Simulated-m2-UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
            case "4xx" -> ResponseEntity.badRequest().body("Simulate 400 error");
            case "5xx" -> ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Simulate 501 error");
            case "slow" -> {
                Thread.sleep(6000L);
                yield ResponseEntity.ok("Simulated-m2-Slowness");
            }
            default -> ResponseEntity.ok("success");
        };
    }
}
