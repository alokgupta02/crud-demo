package com.book.demo.logging;

/**
 * Custom exception for errors occurring in LoggingAspect.
 */
public class LoggingAspectException extends RuntimeException {
    public LoggingAspectException(String message, Throwable cause) {
        super(message, cause);
    }
}
