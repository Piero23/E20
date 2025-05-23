package org.unical.enterprise.gateway.ratelimiter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class RateLimitingGatewayFilter implements GlobalFilter, Ordered {

    private final TokenBucketRateLimiter tokenBucketRateLimiter;

    public RateLimitingGatewayFilter(TokenBucketRateLimiter tokenBucketRateLimiter) {
        this.tokenBucketRateLimiter = tokenBucketRateLimiter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
            .map(addr -> addr.getAddress().getHostAddress())
            .orElse("unknown");
        return Mono.fromCallable(() -> tokenBucketRateLimiter.isAllowed(clientIp))
                .flatMap(allowed -> {
                    if (allowed) {
                        return chain.filter(exchange);
                    } else {
                        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                        return exchange.getResponse().setComplete();
                    }
                });
    }

    @Override
    public int getOrder() {
        return -1; // Set the order of this filter to be the highest priority
    }
}
