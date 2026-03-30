package com.example.ratelimit.application.usecase.cache.l2;

import com.example.ratelimit.domain.models.cache.l2.TokenBucket;
import com.example.ratelimit.domain.repository.cache.l2.TokenBucketRepository;
import com.example.ratelimit.domain.service.cache.l2.TokenBucketService;
import com.example.ratelimit.infrastructure.env.AppEnvironment;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenBucketUseCase implements TokenBucketService {
    TokenBucketRepository tokenBucketRepository;

    @Override
    public Boolean allow(TokenBucket tokenBucket) {
        String key = tokenBucket.key();
        TokenBucket existedTokenBucket = tokenBucketRepository.findByKey(key)
                .orElse(tokenBucketRepository.save(tokenBucket));
        synchronized (existedTokenBucket) {
            existedTokenBucket = existedTokenBucket.refill(System.currentTimeMillis());
            if (tokenBucket.tokens() > 0) {
                tokenBucketRepository.minusTokenByKey(existedTokenBucket.key(), 1);
                return true;
            }

            return false;
        }
    }

    @Override
    public void updateTokens(String key, Double tokens) {
        TokenBucket tokenBucket = tokenBucketRepository.findByKey(key)
                .map(t -> t
                        .withTokens(tokens)
                        .withLastRefillTime(System.currentTimeMillis())
                )
                .orElse(TokenBucket.builder()
                        .tokens(tokens)
                        .refillRate(AppEnvironment.limitForPeriod.doubleValue())
                        .lastRefillTime(System.currentTimeMillis())
                        .build());
        tokenBucketRepository.save(tokenBucket);
    }
}
