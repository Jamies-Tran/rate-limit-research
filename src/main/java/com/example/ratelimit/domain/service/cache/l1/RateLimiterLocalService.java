package com.example.ratelimit.domain.service.cache.l1;

public interface RateLimiterLocalService {
    Boolean allow(String key);

    void reset(String key);
}
