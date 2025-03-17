package com.wgblackmon.docprocessor.contentful.service;
// Core Java imports
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Spring Framework imports
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.catalina.util.RateLimiter;
// import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

// Contentful SDK imports
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cda.CDAContentType;

// Logging
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Custom exceptions
import com.wgblackmon.docprocessor.exception.DocumentNotFoundException;
import com.wgblackmon.docprocessor.exception.ContentfulOperationException;

@Service
public class ContentfulService {
    private final RateLimiter rateLimiter;
    // Rest of the implementation...
    @Value("${contentful.space-id}") String spaceId;
    @Value("${contentful.access-token}") String accessToken);
    private  CDAClient cdaClient = null;
    private final Logger logger = LogManager.getLogger(ContentfulService.class);
        private final Cache<String, CDAEntry> contentCache;
    // private final RateLimiter rateLimiter;

    @Autowired
    public ContentfulService(CDAClient cdaClient, Cache<String, CDAEntry> contentCache, RateLimiter rateLimiter, String accessToken) {
        this.cdaClient = cdaClient;
        this.contentCache = contentCache;
        this.accessToken = accessToken;
        this.rateLimiter = rateLimiter;
    }

    public ContentfulService(String spaceId, String accessToken, Cache<String, CDAEntry> contentCache, RateLimiter rateLimiter) {
        this.accessToken = accessToken;
        this.contentCache = contentCache;
        this.rateLimiter = rateLimiter;



        logger.info("Initializing Contentful client for space: {}", spaceId);



            // The client is thread-safe and should be reused
        this.cdaClient = CDAClient.builder()
                .setSpace(spaceId)      // Your space ID from Contentful
                .setToken(accessToken)   // Your API key
                .build();
    }

    public CDAEntry getDocument(String entryId) {
        logger.debug("Fetching document with ID: {}", entryId);
        try {
            CDAEntry entry = cdaClient.fetch(CDAEntry.class)
                    .one(entryId);

            if (entry == null) {
                logger.warn("Document not found: {}", entryId);
                throw new DocumentNotFoundException(entryId);
            }

            return entry;
        } catch (Exception e) {
            logger.error("Failed to fetch document: {}", entryId, e);
            throw new ContentfulOperationException("Failed to fetch document", e);
        } catch (DocumentNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    public CDAEntry getDocument(String documentId) {
        return contentCache.get(documentId, key ->
                rateLimiter.executeWithLimit(() ->
                        contentfulService.retrieveDocument(key)));
    }
    **/

    public List<CDAEntry> getUntransformedDocuments() {
        logger.debug("Fetching untransformed documents");
        try {
            CDAArray results = cdaClient.fetch(CDAEntry.class)
                    .where("fields.transformed", false)
                    .all();

            logger.info("Found {} untransformed documents",
                    results.total());

            return Arrays.asList(results.items());
        } catch (Exception e) {
            logger.error("Failed to fetch untransformed documents", e);
            throw new ContentfulOperationException("Failed to fetch documents", e);
        }
    }

    public CDAEntry retrieveDocument(String documentId) {
        logger.info("Initiating document retrieval for ID: {}", documentId);

        try {
            CDAEntry document = cdaClient.fetch(CDAEntry.class)
                    .one(documentId);

            if (document == null) {
                throw new DocumentNotFoundException(
                        "Document not found with ID: " + documentId);
            }

            return document;
        } catch (Exception exception) {
            logger.error("Document retrieval failed", exception);
            throw new ContentfulOperationException(
                    "Failed to retrieve document", exception);
        }
    }

    public List<Document> findUnprocessedDocuments() {
        // Build a query for entries with status "unprocessed"
        CDAArray results = cdaClient.fetch(CDAEntry.class)
                .withContentType("document")  // Filter by content type
                .where("fields.status", "unprocessed")
                .orderBy("sys.createdAt")
                .limit(10)
                .all();

        // Convert the results to your domain model
        return Arrays.stream(results.items())
                .map(entry -> convertToDocument((CDAEntry) entry))
                .collect(Collectors.toList());
    }

    public Author getAuthor(CDAEntry documentEntry) {
        // getField returns a linked entry for references
        CDAEntry authorEntry = documentEntry.getField("author");

        return Author.builder()
                .id(authorEntry.id())
                .name(authorEntry.getField("name"))
                .email(authorEntry.getField("email"))
                .build();
    }

    public Document fetchAndTransformDocument(String entryId) {
        try {
            // Fetch the entry from Contentful
            CDAEntry entry = cdaClient.fetch(CDAEntry.class)
                    .one(entryId);

            // Transform the CDAEntry into your domain model
            return Document.builder()
                    .id(entry.id())
                    .title(entry.getField("title"))
                    .content(entry.getField("content"))
                    .status(DocumentStatus.valueOf(entry.getField("status")))
                    .lastModified(entry.updatedAt())
                    .build();

        } catch (CDAResponseException e) {
            // This happens if the API request fails
            throw new ContentfulException(
                    "Failed to fetch document: " + entryId, e);
        } catch (Exception e) {
            // Handle unexpected errors
            throw new ContentfulException(
                    "Error processing document: " + entryId, e);
        }
    }


    public ContentfulService(CDAClient cdaClient, Cache<String, CDAEntry> contentCache, RateLimiter rateLimiter) {
        this.cdaClient = cdaClient;
        this.contentCache = contentCache;
        this.rateLimiter = rateLimiter;
    }


}