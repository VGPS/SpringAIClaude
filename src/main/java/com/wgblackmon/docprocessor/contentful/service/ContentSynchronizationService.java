package com.wgblackmon.docprocessing.contentful.service;
// Core Java imports

// Spring Framework imports
import org.springframework.stereotype.Service;

// Contentful SDK imports
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDASpace;
import com.contentful.java.cda.CDAArray;

// Logging

// Custom models and repositories
import com.wgblackmon.docprocessor.model.SynchronizationResult;
import com.wgblackmon.docprocessor.repository.LocalStorageRepository;

@Service
public class ContentSynchronizationService {
    // Rest of the implementation...

    private final ContentfulService contentfulService;
    private final LocalStorageRepository localRepository;

    public ContentSynchronizationService(ContentfulService contentfulService) {
        this.contentfulService = contentfulService;
    }

    public void synchronizeContent() {
        SynchronizationResult result = contentfulService
                .initiateSync(getLastSyncToken());

        processUpdatedEntries(result.getUpdatedEntries());
        processDeletedEntries(result.getDeletedEntries());
        updateSyncToken(result.getNextSyncToken());
    }

}