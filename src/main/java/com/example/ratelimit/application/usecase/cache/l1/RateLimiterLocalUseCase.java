package com.example.ratelimit.application.usecase.cache.l1;

import com.example.ratelimit.domain.models.cache.l1.RateLimiterProperty;
import com.example.ratelimit.domain.repository.cache.l1.RateLimiterLocalRepository;
import com.example.ratelimit.domain.service.cache.l1.RateLimiterLocalService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimiterLocalUseCase implements RateLimiterLocalService {
    RateLimiterLocalRepository repository;


    @Override
    public Boolean allow(RateLimiterProperty property) {
        String key = property.key();
        Optional<RateLimiter> rateLimiter = repository.findByKey(key);
        if (rateLimiter.isEmpty()) {
            RateLimiter newRateLimiter = repository.save(key, RateLimiter.of(key, rateLimiterConfig(property)));
            return newRateLimiter.acquirePermission();
        }

        return rateLimiter.get().acquirePermission();
    }

    private RateLimiterConfig rateLimiterConfig(RateLimiterProperty property) {
        return RateLimiterProperty.ofDefault(property).config();
    }
}
