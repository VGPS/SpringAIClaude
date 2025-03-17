package com.wgblackmon.docprocessing.config;

import io.micrometer.core.instrument.*;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MetricsConfiguration
 *
 * Configuration class for setting up application metrics and monitoring.
 * Configures Micrometer with Prometheus for metrics collection.
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@Configuration
public class MetricsConfiguration {
    private static final Logger logger = LogManager.getLogger(MetricsConfiguration.class);

    @Bean
    public MeterRegistry meterRegistry() {
        logger.info("Initializing Prometheus meter registry");
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    public MetricsService metricsService(MeterRegistry registry) {
        logger.info("Initializing metrics service");
        return new MetricsService(registry);
    }
}

