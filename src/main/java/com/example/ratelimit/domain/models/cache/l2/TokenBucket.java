package com.example.ratelimit.domain.models.cache.l2;

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
        Double refillRate,
        Long lastRefillTime
) {

     public TokenBucket refill(Long now) {
         long delta = now - lastRefillTime;
         double newTokens = (delta * refillRate) / 1000;
         return TokenBucket.builder()
                 .key(key)
                 .tokens(Math.min(AppEnvironment.bucketCapacity, newTokens + tokens))
                 .lastRefillTime(now)
                 .build();
     }
}
