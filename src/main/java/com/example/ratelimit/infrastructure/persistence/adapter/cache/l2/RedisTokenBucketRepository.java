package com.example.ratelimit.infrastructure.persistence.adapter.cache.l2;

import com.example.ratelimit.domain.models.cache.l2.TokenBucket;
import com.example.ratelimit.domain.repository.cache.l2.TokenBucketRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RedisTokenBucketRepository implements TokenBucketRepository {
    RedisTemplate<String, TokenBucket> redisTemplate;

    @Override
    public TokenBucket save(TokenBucket tokenBucket) {
        redisTemplate.opsForValue().set(tokenBucket.key(), tokenBucket);
        return redisTemplate.opsForValue().get(tokenBucket.key());
    }

    @Override
    public void minusTokenByKey(String key, Integer minusAmount) {
        Optional<TokenBucket> bucket = Optional.ofNullable(redisTemplate.opsForValue().get(key));
        bucket.ifPresent(b -> {
            Double newToken = b.tokens() - minusAmount;
            redisTemplate.opsForValue().set(key, b.withTokens(newToken));
        });
    }

    @Override
    public Optional<TokenBucket> findByKey(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }
}
