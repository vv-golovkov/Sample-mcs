package com.home.m1service.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class Controller {
    private final WebClient m2ServiceClient;
    private final WebClient m3ServiceClient;
    @Value("${cs.m1Name}") // One injection way. Another - see ConfigurationRegistry.class
    private String m1Name;

    public Controller(@Qualifier("m2ServiceClient") WebClient m2ServiceClient,
                      @Qualifier("m3ServiceClient") WebClient m3ServiceClient) {
        this.m2ServiceClient = m2ServiceClient;
        this.m3ServiceClient = m3ServiceClient;
    }

    @GetMapping("/ping")
    public String pingAsync() {
        log.trace("trace logging");
        log.debug("debug logging");
        log.info("info logging");
        //-------------------------
        Mono<String> m2Response = callM2Service();
        Mono<String> m3Response = callM3Service();
        return Mono.zip(m2Response, m3Response).map(set -> {
            String responseFromM2 = set.getT1();
            String responseFromM3 = set.getT2();
            return responseFromM2 + " | " + responseFromM3 + " | " + m1Name;
        }).block();
    }

    private Mono<String> callM2Service() {
        if (m2ServiceClient == null) return Mono.just("m2ServiceClient is null");
        return m2ServiceClient.get().uri("/ping").retrieve().bodyToMono(String.class);
        //.block() - synchronous call (if needed)
    }

    private Mono<String> callM3Service() {
        if (m3ServiceClient == null) return Mono.just("m3ServiceClient is null");
        return m3ServiceClient.get().uri("/ping").retrieve().bodyToMono(String.class);
    }
}
