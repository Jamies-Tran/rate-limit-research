package com.example.ratelimit.adapter.inbound.ratelimit;

import com.example.ratelimit.adapter.annotation.RateLimit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/test")
public interface RateLimitApi {
    @GetMapping
    @RateLimit(key = "USER_IP")
    ResponseEntity<?> test();

    @GetMapping("/no-limiter")
    ResponseEntity<?> testWithoutLimiter();
}
