package com.example.ratelimit.domain.models.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ERateLimitKeyType {
    USER_IP("USER_IP"),
    USER_TOKEN("USER_TOKEN");

    String code;

    public static ERateLimitKeyType findByCode(String code) {
        return Stream.of(values())
                .filter(e -> Objects.equals(e.getCode(), code))
                .findAny()
                .orElse(ERateLimitKeyType.USER_IP);
    }
}
