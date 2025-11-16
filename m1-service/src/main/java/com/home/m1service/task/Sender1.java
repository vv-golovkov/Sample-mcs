package com.home.m1service.task;

import com.home.m1service.task.config.CsConfigurationPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class Sender1 {
    private final CircuitBreaker circuitBreaker;
    private final CsConfigurationPojo pojo;
    private final RestTemplate rest;

    public Sender1(RestTemplate rest, CsConfigurationPojo pojo,
                   CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.rest = rest;
        this.pojo = pojo;
        this.circuitBreaker = circuitBreakerFactory.create("circuitBreakerForM2ServiceCall");
        //by default: ReactiveCircuitBreaker in Spring Cloud already includes 'TimeLimiter[1sec]'
    }

    //@CircuitBreaker - uses Spring AOP.
    //@CircuitBreaker(name = "circuitBreakerForM2ServiceCall", fallbackMethod = "fallback") - not works with reactor
    public ResponseEntity<String> callM2Service_circuitBreaker(String p) {
        return circuitBreaker.run(() -> callM2Service("/rnd/{p}", p), t -> fallback(p, t));
//        return circuitBreaker.run(callM2Service("/rnd/{p}", p), t -> fallback(p, t));
    }

    public ResponseEntity<String> fallback(String p, Throwable t) { //rules: in same class, same signature + Throwable
        return ResponseEntity.internalServerError()
                .body("Try later [%s - %s]".formatted(t.getClass().getSimpleName(), t.getMessage()));
        //---->Possible Exceptions in m2controller.random():
        //---->CallNotPermittedException: CircuitBreaker is OPEN and does not permit further calls
        //un ->WebClientResponseException$InternalServerError
        //ch ->WebClientResponseException$InternalServerError
        //th ->WebClientResponseException$InternalServerError
        //4xx->WebClientResponseException$BadRequest
        //5xx->WebClientResponseException$NotImplemented
        //slow->TimeoutException
    }

    /*
    Resilience4j вважає помилкою тільки явний 'throw exception', а не HTTP статус (викинутий стороннім-сервісом).
    Тому, якщо сторонній-сервіс повертає success response з HttpStatusCode, типу 4xx/5xx:
    - і нам НЕ потрібно, щоб CB рахував його як помилку - можна нічого не робити. By default, Resilience4j will treat this as a success.
    - і нам    потрібно, щоб CB рахував його як помилку - треба обробити response належним чином.
     */
    public ResponseEntity<String> callM2Service(String endpoint, Object... vars) {
        return callService(pojo.getM2ServiceUrl(), endpoint, vars);
    }

    public ResponseEntity<String> callM3Service(String endpoint, Object... vars) {
        return callService(pojo.getM3ServiceUrl(), endpoint, vars);
    }

    private ResponseEntity<String> callService(String url, String endpoint, Object... vars) {
        try {
            log.debug("url={}", url);
            return rest.getForEntity(url + endpoint, String.class, vars);
        } catch (Exception e) {
            log.error("ExceptionDuringCall", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(".%s[%s]".formatted(e.getClass().getSimpleName(), e.getMessage()));
        }
    }
}
