package com.example.ratelimit.domain.service.cache.l2;

public interface TokenBucketService {
    Boolean allow(String key);
}
