package com.example.ratelimit.infrastructure.aspect;

import com.example.ratelimit.adapter.annotation.RateLimit;
import com.example.ratelimit.domain.models.enums.ERateLimitKeyType;
import com.example.ratelimit.domain.service.cache.l1.RateLimiterLocalService;
import com.example.ratelimit.domain.service.cache.l2.TokenBucketService;
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
            String key = extractKey(ERateLimitKeyType.findByCode(rateLimit.key()));
            Boolean checkL1 = rateLimiterLocalService.allow(key);
            if (!checkL1) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                        .body(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
            }
            Boolean checkL2 = tokenBucketService.allow(key);
            if (!checkL2) {
                rateLimiterLocalService.reset(key);
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
}
