package org.unical.enterprise.gateway.ratelimiter.service;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    private final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final double permitsPerSecond = 5.0; // Default rate limit

    public RateLimiterService() {
        this.rateLimiters.computeIfAbsent("default",
                k -> RateLimiter.create(permitsPerSecond));
    }

    public boolean tryAcquireNoWait() {
        RateLimiter rateLimiter = this.rateLimiters.get("default");
        return rateLimiter.tryAcquire();
    }
}