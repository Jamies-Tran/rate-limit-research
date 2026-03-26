package com.example.ratelimit.domain.entity.cache.l1;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;

import java.time.Duration;

public record RateLimiterProperty(
        Integer limitForPeriod,
        Integer limitRefreshPeriod
) {
    public RateLimiterConfig config() {
        return RateLimiterConfig.custom()
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(Duration.ofSeconds(limitRefreshPeriod))
                .timeoutDuration(Duration.ZERO)
                .build();
    }
}
