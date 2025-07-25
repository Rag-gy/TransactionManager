package com.learning.tracker.config;

import com.learning.tracker.config.properties.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
        JwtProperties.class
)
public class ApplicationProperties {
}
