package org.unical.enterprise.gateway.ratelimiter;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.unical.enterprise.gateway.ratelimiter.service.RateLimiterService;
import reactor.core.publisher.Mono;

@Component
@Order(-1)
public class RateLimitWebFilter implements WebFilter {

    private final RateLimiterService rateLimiterService;

    public RateLimitWebFilter(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Apply rate limiting only to API paths
        if (request.getPath().value().startsWith("/api/")) {

            if (!rateLimiterService.tryAcquireNoWait()) {
                return handleRateLimit(exchange);
            }
        }

        return chain.filter(exchange);
    }

    private Mono<Void> handleRateLimit(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

        DataBuffer buffer = response.bufferFactory().wrap("\nTroppe richieste, riprova più tardi.\n".getBytes());
        return response.writeWith(Mono.just(buffer));


    }
}