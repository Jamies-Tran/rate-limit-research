package com.example.ratelimit.adapter.annotation;

import com.example.ratelimit.infrastructure.env.AppEnvironment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    String key() default "";
    String limitForPeriod() default "";
    String limitRefreshPeriod() default "";
}
