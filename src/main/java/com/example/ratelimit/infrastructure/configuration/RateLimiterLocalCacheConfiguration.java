package com.example.ratelimit.infrastructure.configuration;

import com.example.ratelimit.domain.service.cache.l2.TokenBucketService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimiterLocalCacheConfiguration {
    TokenBucketService tokenBucketService;

    @Bean
    public Cache<String, RateLimiter> rateLimiterLocalCache() {
        return Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(1))
                .removalListener((key, value, cause) -> {
                    if (Objects.equals(cause, RemovalCause.EXPIRED)) {
                        RateLimiter rateLimiter = (RateLimiter) value;

                        Double currentTokens = Objects.nonNull(rateLimiter)
                                ? (double) rateLimiter.getMetrics().getAvailablePermissions()
                                : 0.0;
                        tokenBucketService.updateTokens((String) key, currentTokens);
                    }
                })
                .initialCapacity(10000)
                .build();
    }
}
