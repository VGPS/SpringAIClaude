package com.wgblackmon.docprocessing.component;// Core Java imports
import java.util.concurrent.TimeUnit;

// Spring Framework imports
import org.springframework.stereotype.Component;

// Micrometer metrics imports
import io.micrometer.core.instrument.MeterRegistry;

// Logging


@Component
public class ContentfulMetrics {

    private final MeterRegistry registry;

    public ContentfulMetrics(MeterRegistry registry) {
        this.registry = registry;
    }

    // Rest of the implementation...
    public void recordOperation(String operationType, long duration) {
        registry.timer("contentful.operation",
                "type", operationType).record(duration, TimeUnit.MILLISECONDS);
    }
}