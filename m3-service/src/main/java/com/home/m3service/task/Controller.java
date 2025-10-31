package com.home.m3service.task;

import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/s3")
public class Controller {

    @SneakyThrows
    @GetMapping("/ping")
    public String ping() {
        Thread.sleep(4000L);
        return "pong3";
    }
}
