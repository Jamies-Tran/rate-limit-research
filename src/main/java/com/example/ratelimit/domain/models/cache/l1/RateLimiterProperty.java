package com.example.ratelimit.domain.models.cache.l1;

import com.example.ratelimit.infrastructure.env.AppEnvironment;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.Builder;

import java.time.Duration;

@Builder
public record RateLimiterProperty(
        String key,
        Integer limitForPeriod,
        Integer limitRefreshPeriod
) {
    public static RateLimiterProperty ofDefault(RateLimiterProperty property) {
        return RateLimiterProperty.builder()
                .key(property.key())
                .limitForPeriod(property.limitForPeriod)
                .limitRefreshPeriod(property.limitForPeriod)
                .build();
    }

    public RateLimiterConfig config() {
        return RateLimiterConfig.custom()
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(Duration.ofSeconds(limitRefreshPeriod))
                .timeoutDuration(Duration.ZERO)
                .build();
    }
}
