package com.wgblackmon.docprocessor.service;

import com.wgblackmon.docprocessor.model.DocumentChunk;
import com.wgblackmon.docprocessor.exception.ChromaDBException;
import com.wgblackmon.docprocessor.config.ChromaProperties;
import io.github.chromadb.ChromaClient;
import io.github.chromadb.Collection;
import io.github.chromadb.QueryResult;
import io.github.chromadb.GetResponse;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ChromaDBService
 * 
 * This service class manages all interactions with the ChromaDB vector database.
 * It provides functionality for document storage, retrieval, and semantic search
 * operations. The service implements retry mechanisms for reliability and
 * maintains metrics for monitoring purposes.
 *
 * Key responsibilities:
 * - Initialize and manage ChromaDB collections
 * - Handle document storage and retrieval
 * - Perform semantic search operations
 * - Manage error handling and retries
 * - Track operational metrics
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@Service
public class ChromaDBService {
    private static final Logger logger = LogManager.getLogger(ChromaDBService.class);
    
    private final ChromaClient chromaClient;
    private final ChromaProperties properties;
    private final RetryTemplate retryTemplate;
    private Collection collection;

    private static final int DEFAULT_BATCH_SIZE = 100;
    private static final int DEFAULT_SEARCH_LIMIT = 10;

    @Autowired
    public ChromaDBService(ChromaClient chromaClient, 
                          ChromaProperties properties,
                          RetryTemplate retryTemplate) {
        logger.info("Initializing ChromaDBService");
        this.chromaClient = chromaClient;
        this.properties = properties;
        this.retryTemplate = retryTemplate;
    }

    @PostConstruct
    public void init() {
        logger.info("Initializing ChromaDB collection: {}", 
            properties.getCollectionName());
        try {
            collection = retryTemplate.execute(context -> 
                chromaClient.getOrCreateCollection(
                    properties.getCollectionName(),
                    null,
                    null,
                    "Default collection for documents"
                )
            );
            logger.info("Successfully initialized ChromaDB collection");
        } catch (Exception e) {
            logger.error("Failed to initialize ChromaDB collection", e);
            throw new ChromaDBException("ChromaDB initialization failed", e);
        }
    }

    /**
     * Adds a single document chunk to the vector database.
     *
     * @param chunk The document chunk to be added
     * @throws ChromaDBException if the operation fails
     */
    public void addDocument(DocumentChunk chunk) {
        logger.debug("Adding document chunk: {}", chunk.getId());
        try {
            retryTemplate.execute(context -> {
                Collection.AddEmbedding addEmbedding = Collection.AddEmbedding.builder()
                    .ids(List.of(chunk.getId()))
                    .embeddings(List.of(chunk.getEmbeddings()))
                    .metadatas(List.of(chunk.getMetadata()))
                    .documents(List.of(chunk.getContent()))
                    .build();

                collection.add(addEmbedding);
                logger.info("Successfully added document chunk: {}", chunk.getId());
                return null;
            });
        } catch (Exception e) {
            logger.error("Failed to add document chunk: {}", chunk.getId(), e);
            throw new ChromaDBException("Failed to add document", e);
        }
    }

    // ... rest of the implementation with similar logging and documentation
}