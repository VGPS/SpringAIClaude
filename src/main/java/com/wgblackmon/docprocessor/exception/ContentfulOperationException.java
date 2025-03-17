package com.wgblackmon.docprocessor.exception;

public class ContentfulOperationException extends RuntimeException {
    public ContentfulOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}