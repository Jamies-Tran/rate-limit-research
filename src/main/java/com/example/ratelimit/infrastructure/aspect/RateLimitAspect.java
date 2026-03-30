package com.example.ratelimit.infrastructure.aspect;

import com.example.ratelimit.adapter.annotation.RateLimit;
import com.example.ratelimit.domain.models.cache.l1.RateLimiterProperty;
import com.example.ratelimit.domain.models.cache.l2.TokenBucket;
import com.example.ratelimit.domain.models.enums.ERateLimitKeyType;
import com.example.ratelimit.domain.service.cache.l1.RateLimiterLocalService;
import com.example.ratelimit.domain.service.cache.l2.TokenBucketService;
import com.example.ratelimit.infrastructure.env.AppEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimitAspect {
    RateLimiterLocalService rateLimiterLocalService;

    TokenBucketService tokenBucketService;

    HttpServletRequest request;

    @Around("@annotation(rateLimit)")
    public Object handle(ProceedingJoinPoint proceedingJoinPoint, RateLimit rateLimit) {
        try {

            Boolean checkL1 = rateLimiterLocalService.allow(buildRateLimiterProperty(rateLimit));
            if (!checkL1) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
            }
            Boolean checkL2 = tokenBucketService.allow(buildTokenBucket(rateLimit));
            if (!checkL2) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
            }
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private String extractKey(ERateLimitKeyType rateLimitKeyType) {
        return switch (rateLimitKeyType) {
            case USER_IP -> request.getRemoteAddr();
            case USER_TOKEN -> request.getHeader("Authorization");
        };
    }

    private RateLimiterProperty buildRateLimiterProperty(RateLimit rateLimit) {
        String key = extractKey(ERateLimitKeyType.findByCode(rateLimit.key()));
        Integer limitForPeriod = StringUtils.hasText(rateLimit.limitForPeriod())
                ? Integer.parseInt(rateLimit.limitForPeriod())
                : AppEnvironment.limitForPeriod;
        Integer limitRefreshPeriod = StringUtils.hasText(rateLimit.limitRefreshPeriod())
                ? Integer.parseInt(rateLimit.limitRefreshPeriod())
                : AppEnvironment.limitRefreshPeriod;

        return RateLimiterProperty.builder()
                .key(key)
                .limitForPeriod(limitForPeriod)
                .limitRefreshPeriod(limitRefreshPeriod)
                .build();
    }

    private TokenBucket buildTokenBucket(RateLimit rateLimit) {
        String key = extractKey(ERateLimitKeyType.findByCode(rateLimit.key()));
        Double tokens = StringUtils.hasText(rateLimit.limitForPeriod())
                ? Double.parseDouble(rateLimit.limitForPeriod())
                : AppEnvironment.bucketCapacity;
        Double refillRate = StringUtils.hasText(rateLimit.limitRefreshPeriod())
                ? Double.parseDouble(rateLimit.limitRefreshPeriod())
                : AppEnvironment.limitRefreshPeriod;

        return TokenBucket.builder()
                .key(key)
                .tokens(tokens)
                .refillRate(refillRate)
                .lastRefillTime(System.currentTimeMillis())
                .build();
    }
}
