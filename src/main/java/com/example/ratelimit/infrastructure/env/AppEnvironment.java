package com.example.ratelimit.infrastructure.env;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class AppEnvironment {
    public static Integer limitForPeriod;

    public static Integer limitRefreshPeriod;

    public static Double bucketCapacity;

    @Value("${environment.rate-limit.limit-period:1}")
    public void setLimitForPeriod(Integer limitForPeriod) {
        AppEnvironment.limitForPeriod = limitForPeriod;
    }

    @Value("${environment.rate-limit.local.limit-refresh-period:3}")
    public void setLimitRefreshPeriod(Integer limitRefreshPeriod) {
        AppEnvironment.limitRefreshPeriod = limitRefreshPeriod;
    }

    @Value("${environment.rate-limit.distributed.bucket-capacity:5}")
    public void setBucketCapacity(Double bucketCapacity) {
        AppEnvironment.bucketCapacity = bucketCapacity;
    }
}
