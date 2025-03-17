package com.wgblackmon.docprocessor.model;

import lombok.Data;
import lombok.Builder;
import java.util.Map;
import java.util.List;

/**
 * SearchRequest
 * 
 * Model class representing a search request with various search parameters
 * and filters. Supports both semantic and traditional search capabilities.
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@Data
@Builder
public class SearchRequest {
    private String query;
    private int limit;
    private Map<String, Object> filters;
    private List<String> facets;
    private Double minSimilarity;
}