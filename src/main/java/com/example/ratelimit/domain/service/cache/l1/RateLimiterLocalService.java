package com.example.ratelimit.domain.service.cache.l1;

public interface RateLimiterLocalService {
    void consume(String key);
}
