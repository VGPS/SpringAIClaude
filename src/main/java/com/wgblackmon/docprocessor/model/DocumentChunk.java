package com.wgblackmon.docprocessor.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DocumentChunk
 * 
 * Represents a chunk of a document that can be processed and stored
 * in the vector database. Contains the content, embeddings, and metadata
 * associated with a document fragment.
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentChunk {
    private String id;
    private String parentDocumentId;
    private String content;
    private List<Double> embeddings;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime lastModified;
}