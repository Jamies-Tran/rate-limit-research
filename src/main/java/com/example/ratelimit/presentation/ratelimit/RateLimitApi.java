package com.example.ratelimit.presentation.ratelimit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/test")
public interface RateLimitApi {
    @GetMapping
    ResponseEntity<?> test();
}
