package com.home.m2service.task;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/s2")
public class Controller {

    @SneakyThrows
    @GetMapping("/ping")
    public String ping() {
        Thread.sleep(5000L);
        return "pong2";
    }
}
