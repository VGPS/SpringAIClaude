package com.wgblackmon.docprocessor.exception;

// import lombok.Getter;

/**
 * ChromaDBException
 * 
 * Custom exception class for handling ChromaDB-related errors.
 * Provides detailed error information for ChromaDB operations.
 *
 * @author William Blackmon
 * @version 1.0
 * @since 2024-03-01
 */
// @Getter
public class ChromaDBException extends RuntimeException {
    private final ErrorCode errorCode;
    
    public ChromaDBException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ChromaDBException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorCode.UNKNOWN_ERROR;
    }
}

