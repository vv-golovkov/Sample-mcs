package com.home.m1service.task;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class Sender {
    private final ReactiveCircuitBreaker circuitBreaker;
    private final WebClient m2ServiceClient;
    private final WebClient m3ServiceClient;

    public Sender(@Qualifier("m2ServiceClient") WebClient m2ServiceClient,
                  @Qualifier("m3ServiceClient") WebClient m3ServiceClient,
                  ReactiveCircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.m2ServiceClient = m2ServiceClient;
        this.m3ServiceClient = m3ServiceClient;
        this.circuitBreaker = circuitBreakerFactory.create("circuitBreakerForM2ServiceCall");
        //by default: ReactiveCircuitBreaker in Spring Cloud uses both 'CircuitBreaker+TimeLimiter[1s]'
    }

    //@CircuitBreaker - uses Spring AOP.
    //@CircuitBreaker(name = "circuitBreakerForM2ServiceCall", fallbackMethod = "fallback") - not works with reactor
    public Mono<String> callM2Service_circuitBreaker(String p) {
        return circuitBreaker.run(callM2Service("/rnd/{p}", p), t -> fallback(p, t));
    }

    public Mono<String> fallback(String p, Throwable t) { //rules: in same class, same signature + Throwable
        return Mono.just("Try later [%s - %s]".formatted(t.getClass().getSimpleName(), t.getMessage()));
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
    public Mono<String> callM2Service(String endpoint, Object... vars) {
        return m2ServiceClient.get().uri(endpoint, vars).retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)

                /*
                .onStatus(HttpStatusCode::isError, response -> response.bodyToMono(String.class)
                        .defaultIfEmpty("Http Error without body")
                        .flatMap(body -> Mono.error(new RuntimeException("HTTP Error: " + body)))
                )
                 */

                .bodyToMono(String.class);
    }

    public Mono<String> callM3Service(String endpoint) {
        return m3ServiceClient.get().uri(endpoint).retrieve().bodyToMono(String.class);
    }
}
