package com.example.ratelimit.presentation.ratelimit;

import com.example.ratelimit.domain.service.cache.l1.RateLimiterLocalService;
import com.example.ratelimit.domain.service.cache.l2.TokenBucketService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimitController implements RateLimitApi {
    RateLimiterLocalService rateLimiterLocalService;
    TokenBucketService tokenBucketService;
    HttpServletRequest request;

    @Override
    public ResponseEntity<?> test() {
        String key = extractKey();
        Boolean localCachePass = rateLimiterLocalService.allow(key);
        if (!localCachePass) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        }
        Boolean tokenBucketPass = tokenBucketService.allow(key);
        if (!tokenBucketPass) {
            rateLimiterLocalService.reset(key);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
        }

        return ResponseEntity.ok().body("Success!");
    }

    private String extractKey() {
        return request.getRemoteAddr();
    }
}
