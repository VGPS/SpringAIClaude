package com.wgblackmon.docprocessing.utils.monitoring;

/**
 * SearchResultCache
 *
 * Specialized cache for search results with custom eviction policies.
 */
@Component
@Slf4j
public class SearchResultCache {
    private final Cache<String, List<SearchResult>> searchCache;
    private final MetricsService metricsService;

    public SearchResultCache(MetricsService metricsService) {
        this.metricsService = metricsService;
        this.searchCache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .removalListener(this::onRemoval)
                .build();
    }

    public Optional<List<SearchResult>> getSearchResults(SearchRequest request) {
        String cacheKey = generateCacheKey(request);
        return Optional.ofNullable(searchCache.getIfPresent(cacheKey));
    }

    public void cacheSearchResults(SearchRequest request,
                                   List<SearchResult> results) {
        String cacheKey = generateCacheKey(request);
        searchCache.put(cacheKey, results);
        logger.debug("Cached search results for key: {}", cacheKey);
    }

    private String generateCacheKey(SearchRequest request) {
        return String.format("%s-%d-%s",
                request.getQuery(),
                request.getLimit(),
                request.getFilters().toString());
    }

    private void onRemoval(String key, List<SearchResult> value,
                           RemovalCause cause) {
        logger.debug("Removing search results from cache. Key: {}, Cause: {}",
                key, cause);
        metricsService.recordCacheEviction("search-cache", cause.toString());
    }
}
