package com.home.m2service.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Thread.sleep(3000L);
        return "pong2[%s, %s]".formatted(m2Name, forAll);
    }
}
