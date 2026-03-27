package com.example.ratelimit.domain.entity.cache.l2;

import com.example.ratelimit.infrastructure.env.AppEnvironment;
import lombok.Builder;

@Builder
public record TokenBucket(
        String key,
        Double tokens,
        Long lastRefillTime
) {
    public static TokenBucket ofDefault(String key) {
        return TokenBucket.builder()
                .key(key)
                .tokens(AppEnvironment.bucketCapacity)
                .lastRefillTime(System.currentTimeMillis())
                .build();
    }

     public TokenBucket refill(Long now) {
         long delta = now - lastRefillTime;
         if (delta >= 1000) {
             double newTokens = (double) (delta * AppEnvironment.tokenRefillRate) / 1000;
             return TokenBucket.builder()
                     .key(key)
                     .tokens(Math.min(AppEnvironment.bucketCapacity, newTokens + tokens))
                     .lastRefillTime(now)
                     .build();
         }

         return this;
     }
}
