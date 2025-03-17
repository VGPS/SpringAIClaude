package com.wgblackmon.docprocessing.utils.monitoring;


/**
 * CacheMonitor
 *
 * Component for monitoring cache performance and health.
 */
@Component
@Slf4j
public class CacheMonitor {
    private final MetricsService metricsService;
    private final Map<String, Cache<?, ?>> monitoredCaches;

    @Scheduled(fixedRate = 60000) // Every minute
    public void monitorCacheMetrics() {
        monitoredCaches.forEach((name, cache) -> {
            CacheStats stats = cache.stats();
            recordCacheMetrics(name, stats);
        });
    }

    private void recordCacheMetrics(String cacheName, CacheStats stats) {
        metricsService.gauge("cache.size", cacheName,
                monitoredCaches.get(cacheName).estimatedSize());
        metricsService.gauge("cache.hit.rate", cacheName,
                stats.hitRate());
        metricsService.gauge("cache.miss.rate", cacheName,
                stats.missRate());
        metricsService.gauge("cache.eviction.count", cacheName,
                stats.evictionCount());
    }
}