package com.nayanzin.edgeservice.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("throttled")
@Configuration
public class ThrottlingConfiguration {

    @Bean
    @SuppressWarnings("UnstableApiUsage")
    public RateLimiter rateLimiter() {
        return RateLimiter.create(1.0D / 5.0D);
    }
}
