package com.example.ratelimit.adapter.inbound.ratelimit;

import com.example.ratelimit.adapter.annotation.RateLimit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RateLimitController implements RateLimitApi {

    @Override
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().body("Success!");
    }

    @Override
    public ResponseEntity<?> testWithoutLimiter() {
        return ResponseEntity.ok().body("Success!");
    }
}
