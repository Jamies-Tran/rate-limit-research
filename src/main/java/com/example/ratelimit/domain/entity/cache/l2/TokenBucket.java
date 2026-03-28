package com.example.ratelimit.domain.entity.cache.l2;

import com.example.ratelimit.infrastructure.env.AppEnvironment;
import lombok.Builder;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record TokenBucket(
        String key,
        @With
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
         if (delta >= AppEnvironment.limitRefreshPeriod) {
             double newTokens = (double) (delta * AppEnvironment.limitForPeriod) / 1000;
             return TokenBucket.builder()
                     .key(key)
                     .tokens(Math.min(AppEnvironment.bucketCapacity, newTokens + tokens))
                     .lastRefillTime(now)
                     .build();
         }

         return this;
     }
}
