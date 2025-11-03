package com.home.m3service.task;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller {
    @Value("${cs.m3Name}")
    private String m3Name;
    @Value("${cs.forall}")
    private String forAll;

    @SneakyThrows
    @GetMapping("/ping")
    public String ping() {
        Thread.sleep(4000L);
        return "pong3[%s, %s]".formatted(m3Name, forAll);
    }
}
