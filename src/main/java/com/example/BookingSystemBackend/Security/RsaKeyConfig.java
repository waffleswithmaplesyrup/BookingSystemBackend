package com.example.BookingSystemBackend.Security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RsaKeyProperties.class)
public class RsaKeyConfig {
}

// References:
// -- maintain separation of concerns without modifying the main class (i.e.,
// without adding `@EnableConfigurationProperties(RsaKeyProperties.class)` to
// the main class): https://www.baeldung.com/spring-enable-config-properties
// -- alternatively, use `@ConfigurationPropertiesScan` to replace the
// `@EnableConfigurationProperties(RsaKeyProperties.class)` in this file,
// which would also work
