package com.home.m1service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

//-> DIRECTLY                           (http://localhost:8081/ping)
//-> GATEWAY_SERVICE_DISCOVERY_AUTO     (http://localhost:8090/m1-service/ping)
//-> GATEWAY_SERVICE_DISCOVERY_ROUTES   (http://localhost:8090/m1/ping);
@Slf4j
@RestController
@RequiredArgsConstructor //generate constructors ONLY for FINAL vars
public class M1Controller {
    //@Value("${other.pwd}") //it is not refreshable within this Controller, but works with Vault!
    private String otherPwd;
    private final String INSTANCE = System.getenv().getOrDefault("HOSTNAME", "unk");
    private final Sender1 sender;

    @GetMapping("/ping")
    public String ping() {
        log.trace("m1:trace");
        log.debug("m1:debug");
        log.info("m1:info");
        log.warn("m1:warn [instance={}]", INSTANCE);
        return "pong1(%s)".formatted(INSTANCE);
    }

    @GetMapping("/cb/{p}")
    public ResponseEntity<String> pingCircuitBreaker(@PathVariable String p) {
        return sender.callM2Service_circuitBreaker(p);
    }

    @GetMapping("/call3")
    //If using WebFlux - each method should always return Mono/Flux ( +never use .block() )
    public ResponseEntity<String> callM3() {
        log.info("M1Controller.callM3()...");
        return sender.callM3Service("/ping");
    }
}
