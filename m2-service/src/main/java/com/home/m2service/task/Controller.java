package com.home.m2service.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

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
        log.debug("received ping request...");
        Thread.sleep(3000L);
        log.debug("replying success {}", m2Name);
        if (Math.random() > 0.5) throw new IOException("simulated-m2-io");
        return "pong2[%s, %s]".formatted(m2Name, forAll);
    }

    @SneakyThrows
    @GetMapping("/rnd/{p}")
    public Mono<ResponseEntity<String>> random(String p) {
        Mono<ResponseEntity<String>> r = switch (p) {
            case "un" -> Mono.error(new IllegalStateException("Simulated-m2-RuntimeException"));
            case "ch" -> Mono.error(new IOException("Simulated-m2-IOException"));
            case "th" -> throw new RuntimeException("Throw-m2-RuntimeException");
            //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Simulated 401 error");
            //return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Simulated-m2-UNAUTHORIZED"));
            //return new ResponseEntity<>("Simulated-m2-UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
            case "4xx" -> Mono.just(ResponseEntity.badRequest().body("Simulate 400 error"));
            case "5xx" -> Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Simulate 501 error"));
            case "slow" -> Mono.delay(Duration.ofSeconds(6)).thenReturn(ResponseEntity.ok("Simulated-m2-Slowness"))
                    .doOnSuccess(re -> log.info("Terminate slow response: {}", re.getStatusCode()))
                    .doOnCancel(() -> log.info("Cancel slow response")); //when somebody unsubscribes from Mono
            default -> Mono.just(ResponseEntity.ok("success"));
        };

        log.info("Method /random completed execution: {}", p); //FYI: executed asynchronously before Mono completes
        return r;
    }
}
