package com.example.ratelimit.infrastructure.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterLocalCacheConfiguration {
    @Bean
    public Cache<String, RateLimiter> rateLimiterLocalCache() {
        return Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofMinutes(1))
                .initialCapacity(10000)
                .build();
    }
}
