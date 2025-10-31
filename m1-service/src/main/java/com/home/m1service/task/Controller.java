package com.home.m1service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
//@RequestMapping("/s1")
public class Controller {
    private final WebClient m2ServiceClient;
    private final WebClient m3ServiceClient;

    public Controller(@Qualifier("m2ServiceClient") WebClient m2ServiceClient,
                      @Qualifier("m3ServiceClient") WebClient m3ServiceClient) {
        this.m2ServiceClient = m2ServiceClient;
        this.m3ServiceClient = m3ServiceClient;
    }

    @GetMapping("/ping")
    public String pingAsync() {
        Mono<String> m2Response = callM2Service();
        Mono<String> m3Response = callM3Service();
        return Mono.zip(m2Response, m3Response).map(set -> {
            String responseFromM2 = set.getT1();
            String responseFromM3 = set.getT2();
            return responseFromM2 + " | " + responseFromM3;
        }).block();
    }

    private Mono<String> callM2Service() {
        return m2ServiceClient.get().uri("/ping").retrieve().bodyToMono(String.class);
        //.block() - synchronous call
    }

    private Mono<String> callM3Service() {
        return m3ServiceClient.get().uri("/ping").retrieve().bodyToMono(String.class);
    }
}
