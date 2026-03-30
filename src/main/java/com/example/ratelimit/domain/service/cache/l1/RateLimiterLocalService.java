package com.example.ratelimit.domain.service.cache.l1;


import com.example.ratelimit.domain.models.cache.l1.RateLimiterProperty;

public interface RateLimiterLocalService {
    Boolean allow(RateLimiterProperty property);
}
