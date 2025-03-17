package com.wgblackmon.docprocessor.controller;

import com.wgblackmon.docprocessor.model.SearchRequest;
import com.wgblackmon.docprocessor.model.SearchResult;
import com.wgblackmon.docprocessor.service.AdvancedSearchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * AdvancedSearchController
 * 
 * REST controller handling advanced search operations for the document processing system.
 * Provides endpoints for semantic search and hybrid search capabilities.
 *
 * Key responsibilities:
 * - Handle semantic search requests
 * - Process hybrid search operations
 * - Manage search result responses
 * - Validate search parameters
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@RestController
@RequestMapping("/api/search")
@Tag(name = "Search API", description = "Endpoints for document searching")
public class AdvancedSearchController {
    private static final Logger logger = LogManager.getLogger(AdvancedSearchController.class);
    
    private final AdvancedSearchService searchService;

    @Autowired
    public AdvancedSearchController(AdvancedSearchService searchService) {
        this.searchService = searchService;
        logger.info("Initializing AdvancedSearchController");
    }

    @PostMapping("/hybrid")
    @Operation(summary = "Perform hybrid search", 
              description = "Combines semantic and traditional search capabilities")
    public ResponseEntity<List<SearchResult>> hybridSearch(@RequestBody SearchRequest request) {
        logger.debug("Received hybrid search request: {}", request);
        try {
            List<SearchResult> results = searchService.performHybridSearch(request);
            logger.info("Successfully completed hybrid search, found {} results", 
                results.size());
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error performing hybrid search", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}