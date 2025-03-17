package com.wgblackmon.docprocessor.config;

// Contentful SDK imports
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDASpace;


import lombok.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;

public class ContentfulConfiguration {

    @Value("${contentful.space-id}")
    private String spaceId;

    @Value("${contentful.access-token}")
    private String accessToken;

    @Bean
    public CDAClient contentfulClient() {
        return CDAClient.builder()
                .setSpace(spaceId)
                .setToken(accessToken)
                .build();
    }
}

