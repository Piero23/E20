package org.unical.enterprise.gateway.ratelimiter;

import org.springframework.stereotype.Component;
import com.google.common.util.concurrent.RateLimiter;
import org.unical.enterprise.gateway.config.RateLimiterProperties;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBucketRateLimiter {
    private final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final RateLimiterProperties rateLimiterProperties;

    public TokenBucketRateLimiter(RateLimiterProperties rateLimiterProperties) {
        this.rateLimiterProperties = rateLimiterProperties;
    }

    public boolean isAllowed(String key) {
        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(key, k -> RateLimiter.create(rateLimiterProperties.getPermitsPerSecond()));
        return rateLimiter.tryAcquire();
    }
}
