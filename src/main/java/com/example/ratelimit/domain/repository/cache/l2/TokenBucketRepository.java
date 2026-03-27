package com.example.ratelimit.domain.repository.cache.l2;

import com.example.ratelimit.domain.entity.cache.l2.TokenBucket;

import java.util.Optional;

public interface TokenBucketRepository {
     void save(TokenBucket tokenBucket);

     void minusTokenByKey(String key, Integer minusAmount);

     Optional<TokenBucket> findByKey(String key);
}
