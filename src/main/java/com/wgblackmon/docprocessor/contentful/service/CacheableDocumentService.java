package com.wgblackmon.docprocessing.contentful.service;

/**
 * CacheableDocumentService
 *
 * Service implementation that uses Spring's caching annotations
 * for declarative caching.
 */
@Service
@Slf4j
public class CacheableDocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentCache documentCache;

    @Cacheable(value = "documents", key = "#id", unless = "#result == null")
    public Document getDocument(String id) {
        logger.debug("Fetching document: {}", id);
        return documentRepository.findById(id)
                .orElseThrow(() -> new DocumentNotFoundException(id));
    }

    @Cacheable(value = "document-chunks", key = "#documentId")
    public List<DocumentChunk> getDocumentChunks(String documentId) {
        logger.debug("Fetching chunks for document: {}", documentId);
        return documentRepository.findChunksByDocumentId(documentId);
    }

    @CachePut(value = "documents", key = "#document.id")
    public Document updateDocument(Document document) {
        logger.debug("Updating document: {}", document.getId());
        return documentRepository.save(document);
    }

    @CacheEvict(value = "documents", key = "#id")
    public void deleteDocument(String id) {
        logger.debug("Deleting document: {}", id);
        documentRepository.deleteById(id);
    }
}

