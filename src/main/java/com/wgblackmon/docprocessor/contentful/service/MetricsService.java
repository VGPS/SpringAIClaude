package com.wgblackmon.docprocessing.contentful.service;

/**
 * MetricsService
 *
 * Core service for managing application metrics and monitoring.
 */

@Service
@Slf4j
public class MetricsService {
    private final MeterRegistry registry;
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();
    private final Map<String, Gauge> gauges = new ConcurrentHashMap<>();

    @Autowired
    public MetricsService(MeterRegistry registry) {
        this.registry = registry;
        initializeMetrics();
    }

    private void initializeMetrics() {
        // Document Processing Metrics
        Counter.builder("documents.processed.total")
                .description("Total number of documents processed")
                .register(registry);

        Timer.builder("document.processing.time")
                .description("Time taken to process documents")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        // Search Metrics
        Counter.builder("search.requests.total")
                .description("Total number of search requests")
                .register(registry);

        Timer.builder("search.request.time")
                .description("Time taken for search requests")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(registry);

        Counter.builder("processing.errors")
                .description("Number of processing errors")
                .register(registry);
    }

    public void recordDocumentProcessed(String documentId, long processingTime) {
        registry.counter("documents.processed.total").increment();
        registry.timer("document.processing.time").record(processingTime, TimeUnit.MILLISECONDS);
        logger.debug("Recorded document processing metrics for: {}", documentId);
    }

    public void recordSearchMetrics(SearchRequest request, long searchTime) {
        registry.counter("search.requests.total").increment();
        registry.timer("search.request.time").record(searchTime, TimeUnit.MILLISECONDS);
        logger.debug("Recorded search metrics for request: {}", request.getQuery());
    }

    public void recordError(String errorType) {
        registry.counter("processing.errors", "type", errorType).increment();
        logger.warn("Recorded processing error of type: {}", errorType);
    }
}





