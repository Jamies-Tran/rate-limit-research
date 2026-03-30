package com.example.ratelimit.domain.service.cache.l2;

import com.example.ratelimit.domain.models.cache.l2.TokenBucket;

public interface TokenBucketService {
    Boolean allow(TokenBucket tokenBucket);

    void updateTokens(String key, Double tokens);
}
