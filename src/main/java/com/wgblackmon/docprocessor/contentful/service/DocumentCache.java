package com.wgblackmon.docprocessing.contentful.service;

/**
 * DocumentCache
 *
 * Service for managing document-specific caching operations.
 */
@Service
@Slf4j
public class DocumentCache {
    private final Cache<String, Document> documentCache;
    private final Cache<String, List<DocumentChunk>> chunkCache;
    private final MetricsService metricsService;

    public DocumentCache(MetricsService metricsService) {
        this.metricsService = metricsService;
        this.documentCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats()
                .build();

        this.chunkCache = Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats()
                .build();

        scheduleMetricsCollection();
    }

    public void cacheDocument(Document document) {
        logger.debug("Caching document: {}", document.getId());
        documentCache.put(document.getId(), document);
    }

    public Optional<Document> getDocument(String id) {
        return Optional.ofNullable(documentCache.getIfPresent(id));
    }

    public void cacheChunks(String documentId, List<DocumentChunk> chunks) {
        logger.debug("Caching {} chunks for document: {}",
                chunks.size(), documentId);
        chunkCache.put(documentId, chunks);
    }

    public Optional<List<DocumentChunk>> getChunks(String documentId) {
        return Optional.ofNullable(chunkCache.getIfPresent(documentId));
    }

    private void scheduleMetricsCollection() {
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::recordCacheMetrics,
                        1, 1, TimeUnit.MINUTES);
    }

    private void recordCacheMetrics() {
        CacheStats documentStats = documentCache.stats();
        CacheStats chunkStats = chunkCache.stats();

        metricsService.recordCacheMetrics("document-cache", documentStats);
        metricsService.recordCacheMetrics("chunk-cache", chunkStats);
    }
}