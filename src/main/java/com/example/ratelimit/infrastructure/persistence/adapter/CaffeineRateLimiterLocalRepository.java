package com.example.ratelimit.infrastructure.persistence.adapter;

import com.example.ratelimit.domain.repository.cache.l1.RateLimiterLocalRepository;
import com.github.benmanes.caffeine.cache.Cache;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CaffeineRateLimiterLocalRepository implements RateLimiterLocalRepository {
    Cache<String, RateLimiter> rateLimiterLocalCache;
    
    @Override
    public void save(String key, RateLimiter rateLimiter) {
        rateLimiterLocalCache.put(key, rateLimiter);
    }

    @Override
    public Optional<RateLimiter> findByKey(String key) {
        return Optional.ofNullable(rateLimiterLocalCache.getIfPresent(key));
    }
}
