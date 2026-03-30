package com.example.ratelimit.domain.service.cache.l1;


import com.example.ratelimit.domain.models.cache.l1.RateLimiterProperty;
import io.github.resilience4j.ratelimiter.RateLimiter;

import java.util.Optional;

public interface RateLimiterLocalService {
    Boolean allow(RateLimiterProperty property);

    Optional<RateLimiter> findByKey(String key);
}
