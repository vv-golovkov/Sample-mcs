package com.home.m1service.task;

import com.home.m1service.task.config.CsConfigurationPojo;
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
    private final CsConfigurationPojo csConfigurationPojo;
    private final Sender1 sender;

    @GetMapping("/hi")
    public String hi() {
        return "Hi - from M1";
    }

    @GetMapping("/cb/{p}")
    public ResponseEntity<String> pingCircuitBreaker(@PathVariable String p) {
        return sender.callM2Service_circuitBreaker(p);
    }

    @GetMapping("/block")
    //If using WebFlux - each method should always return Mono/Flux ( +never use .block() )
    public ResponseEntity<String> pingBlock() {
        log.trace("m1:trace");
        log.debug("m1:debug");
        log.info("m1:info");
        log.warn("m1:warn");
        log.debug("csConfigurationPojo [{}, {}]", csConfigurationPojo.hashCode(), csConfigurationPojo);
        if (csConfigurationPojo.getM3ServiceUrl() == null) {
            return ResponseEntity.ok("M3 URL is null");
        }
        return sender.callM3Service("/ping");
    }
}
