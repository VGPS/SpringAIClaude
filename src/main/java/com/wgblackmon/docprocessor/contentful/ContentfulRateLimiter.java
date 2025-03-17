package com.wgblackmon.docprocessing.contentful;

public class ContentfulRateLimiter {

    private final RateLimiter rateLimiter = RateLimiter.create(60.0); // 60 requests per minute

    public <T> T executeWithRateLimit(Supplier<T> operation) {
        rateLimiter.acquire(); // This will block if we're exceeding the rate limit
        return operation.get();
    }


}
