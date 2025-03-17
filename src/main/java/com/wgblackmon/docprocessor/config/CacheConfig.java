package com.wgblackmon.docprocessor.config;

import org.apache.logging.log4j.LogManager;
// import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.concurrent.TimeUnit;

/**
 * CacheConfig
 *
 * Configuration class for setting up application-wide caching.
 * Uses Caffeine as the caching provider with customized cache specifications.
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@Configuration
@EnableCaching
public class CacheConfig {
    private static final Logger logger = (Logger) LogManager.getLogger(CacheConfig.class);

    @Bean
    public CacheManager cacheManager() {
        logger.info("Initializing Caffeine cache manager");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        HttpClient Caffeine = null;
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10_000)
                .recordStats());
        return cacheManager;
    }

    @Bean
    public CacheProperties.Caffeine<Object, Object> caffeineConfig() {
        HttpClient Caffeine;
        return Caffeine.newBuilder()
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .initialCapacity(100)
                .maximumSize(10_000);
    }
}