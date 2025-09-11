package org.unical.enterprise.gateway.ratelimiter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unical.enterprise.gateway.ratelimiter.service.RateLimiterService;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final RateLimiterService rateLimiterService;

    public ApiController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> getData() {
        return Mono.just(ResponseEntity.ok("Richiesta GET processata con successo"));
    }

    @PostMapping
    public Mono<ResponseEntity<String>> processData() {
        if (!rateLimiterService.tryAcquire(Duration.ofSeconds(1))) {
            return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Troppe richieste, riprova più tardi"));
        }

        return Mono.just(ResponseEntity.ok("Richiesta POST processata con successo"));
    }
}