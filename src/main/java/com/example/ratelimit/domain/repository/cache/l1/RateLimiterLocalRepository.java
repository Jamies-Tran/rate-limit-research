package com.example.ratelimit.domain.repository.cache.l1;

import io.github.resilience4j.ratelimiter.RateLimiter;

import java.util.Optional;

public interface RateLimiterLocalRepository {
    void save(String key, RateLimiter rateLimiter);

    Optional<RateLimiter> findByKey(String key);
}
