package com.example.ratelimit.domain.models.ratelimiter;

import com.example.ratelimit.domain.models.cache.l1.RateLimiterProperty;
import com.example.ratelimit.domain.models.cache.l2.TokenBucket;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.Builder;

@Builder
public record RateLimiter(
        String key,
        Integer limitForPeriod,
        Integer limitRefreshPeriod
) {
    public RateLimiterProperty rateLimiterProperty() {
        return RateLimiterProperty.builder()
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(limitRefreshPeriod)
                .build();
    }

    public TokenBucket tokenBucket() {
        return TokenBucket.builder()
                .key(key)
                .tokens(limitForPeriod.doubleValue())
                .build();
    }
}
