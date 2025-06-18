package com.book.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.book.demo.response.ApiResponse;
import com.book.demo.constants.ApiMessages;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import com.book.demo.logging.LoggingAspectException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBookNotFound(BookNotFoundException ex, HttpServletRequest request) {
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ApiMessages.BOOK_NOT_FOUND.getMessage(),
            null,
            Collections.singletonList(ApiMessages.BOOK_NOT_FOUND.getMessage()),
            404,
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ApiMessages.INVALID_BOOK_DATA.getMessage(),
            null,
            Collections.singletonList(errorMsg),
            400,
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOther(Exception ex, HttpServletRequest request) {
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ApiMessages.INTERNAL_ERROR.getMessage(),
            null,
            Collections.singletonList(ApiMessages.INTERNAL_ERROR.getMessage()),
            500,
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(LoggingAspectException.class)
    public ResponseEntity<ApiResponse<Void>> handleLoggingAspectException(LoggingAspectException ex, HttpServletRequest request) {
        Throwable cause = ex.getCause();
        if (cause instanceof BookNotFoundException bookNotFoundException) {
            return handleBookNotFound(bookNotFoundException, request);
        }
        ApiResponse<Void> response = new ApiResponse<>(
            false,
            ApiMessages.INTERNAL_ERROR.getMessage(),
            null,
            Collections.singletonList(ApiMessages.INTERNAL_ERROR.getMessage()),
            500,
            request.getRequestURI()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
