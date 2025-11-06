package com.home.m1service.task;

import com.home.common.MyPersonDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
public class Controller {
    private final Sender sender;
    @Value("${cs.m1Name}") // One injection way. Another - see ConfigurationRegistry.class
    private String m1Name;

    public Controller(Sender sender) {
        this.sender = sender;
    }

    @GetMapping("/ping")
    public Mono<String> pingAsync() {
        MyPersonDTO p = new MyPersonDTO("vg", 29);
//        Object p = new Object();
        log.trace("this is trace");
        log.debug("this is debug");
        log.info("this is info + {}", p);
        //-------------------------
        Mono<String> m2Response = sender.callM2Service("/ping");
        Mono<String> m3Response = sender.callM3Service("/ping");
        return Mono.zip(m2Response, m3Response)
                .map(set -> set.getT1() + " | " + set.getT2() + " | " + m1Name)
                .map(this::finishMe)
                .onErrorResume(RuntimeException.class, e -> {
                    log.error("ErrCaught in /ping processing", e);
                    return Mono.just("Default-ping-response-from-m1");
                });
    }

    private String finishMe(String r) {
        log.debug("I received result: {}", r);
        if (Math.random() > 0.5) throw new RuntimeException("simulated-m1-runtime");
        return "Finish->" + r;
    }

    @GetMapping("/cb/{p}")
    public Mono<String> pingCircuitBreaker(@PathVariable String p) {
        return sender.callM2Service_circuitBreaker(p);
    }
}
