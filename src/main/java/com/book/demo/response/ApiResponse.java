package com.book.demo.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import lombok.Data;
@Data
/**
 * ApiResponse is a generic class that represents the structure of API responses.
 * It includes fields for success status, message, data, errors, error code, timestamp, and path.
 *
 * @param <T> the type of data included in the response
 */
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private int errorCode;
    private String timestamp;
    private String path;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss-dd:MM:yyyy"));
    }

    public ApiResponse(boolean success, String message, T data, List<String> errors, int errorCode, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss-dd:MM:yyyy"));
        this.path = path;
    }


}
