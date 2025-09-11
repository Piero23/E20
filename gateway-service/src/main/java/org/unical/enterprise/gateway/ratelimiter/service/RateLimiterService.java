package org.unical.enterprise.gateway.ratelimiter.service;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {
    private final RateLimiter rateLimiter;

    public RateLimiterService() {
        this.rateLimiter = RateLimiter.create(10.0); // 10 permits per second
    }

    public boolean tryAcquire() {
        return rateLimiter.tryAcquire();
    }

    public boolean tryAcquire(Duration timeout) {
        return rateLimiter.tryAcquire(timeout.toMillis(), TimeUnit.MILLISECONDS);
    }

    public void acquire() {
        rateLimiter.acquire();
    }
}