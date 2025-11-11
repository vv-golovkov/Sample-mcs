package com.home.m1service.task;

import com.home.common.MyPersonDTO;
import com.home.m1service.task.config.CsConfigurationPojo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor //generate constructors ONLY for FINAL vars
public class M1Controller {
    private final CsConfigurationPojo csConfigurationPojo;
    private final Sender sender;

    @GetMapping("/block")
    public Mono<String> pingBlock() { //in webflux, method should always return Mono/Flux ( +never use .block() )
        log.debug("pingBlock.start");
        log.debug("csConfigurationPojo [{}, {}]", csConfigurationPojo.hashCode(), csConfigurationPojo);
        return sender.callM3Service("/ping").doOnNext(r -> log.debug("pingBlock.finish: {}", r));
    }

    @GetMapping("/ping")
    public Mono<String> pingAsync() {
        MyPersonDTO p = new MyPersonDTO("vg", 29);
//        Object p = new Object();
        log.trace("m2:trace");
        log.debug("m2:debug");
        log.info("m2:info + {}", p);
        log.warn("m2:warn");
        //-------------------------
        Mono<String> m2Response = sender.callM2Service("/ping");
        Mono<String> m3Response = sender.callM3Service("/ping");
        return Mono.zip(m2Response, m3Response)
                .map(set -> set.getT1() + " | " + set.getT2())
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
