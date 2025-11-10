package com.home.m2service.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class Controller {
    @Value("${cs.m2Name}")
    private String m2Name;
    @Value("${cs.forall}")
    private String forAll;

    @SneakyThrows
    @GetMapping("/ping")
    public String ping() {
        log.trace("m2:trace");
        log.debug("m2:debug");
        log.info("m2:info");
        log.warn("m2:warn");

        log.debug("received ping request...");
        Thread.sleep(3000L);
        log.debug("replying success {}", m2Name);
        //if (Math.random() > 0.5) throw new IOException("simulated-m2-io");
        return "pong2[%s, %s]".formatted(m2Name, forAll);
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
