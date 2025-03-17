package com.wgblackmon.docprocessing.utils.monitoring;

/**
 * SystemHealthMetrics
 *
 * Monitors system health and resource utilization.
 */
@Component
@Slf4j
public class SystemHealthMetrics {
    private final MeterRegistry registry;

    @Scheduled(fixedRate = 60000) // Every minute
    public void recordSystemMetrics() {
        // Memory metrics
        Runtime runtime = Runtime.getRuntime();
        Gauge.builder("jvm.memory.used", runtime, this::getUsedMemory)
                .description("JVM used memory")
                .register(registry);

        Gauge.builder("jvm.memory.total", runtime, Runtime::totalMemory)
                .description("JVM total memory")
                .register(registry);

        // Thread metrics
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        Gauge.builder("jvm.threads.active", threadBean, ThreadMXBean::getThreadCount)
                .description("Active thread count")
                .register(registry);
    }

    private double getUsedMemory(Runtime runtime) {
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
