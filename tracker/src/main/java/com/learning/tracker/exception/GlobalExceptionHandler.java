package com.learning.tracker.exception;

import com.learning.tracker.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.NoHandlerFoundException;


import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpServletRequest request
    ){
        log.error("No handler found for request: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "NOT_FOUND",
                "The requested resource could not be found",
                "Please check the URL or contact support",
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request
    ){
        log.error("Method not supported: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "METHOD_NOT_ALLOWED",
                "The requested method is not allowed for this resource",
                "Please check the request method and try again",
                request.getRequestURI(),
                HttpStatus.METHOD_NOT_ALLOWED.value()
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ){
        log.error("Validation error: {}", ex.getMessage());
        String errorMessage = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "VALIDATION_ERROR",
                "Validation failed: " + errorMessage,
                "Please check your request data",
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()

        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDTO> handleBindException(
            BindException ex,
            HttpServletRequest request
    ){
        log.error("Bind error: {}", ex.getMessage());
        String errorMessage = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .collect(Collectors.joining(", "));

        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "VALIDATION_ERROR",
                "Invalid request data: " + errorMessage,
                "Please check your request data",
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ){
        log.error("Malformed JSON request: {}", ex.getMessage());

        String message = "Invalid JSON format";
        if (ex.getMessage().contains("Cannot deserialize value")) {
            message = "Invalid value for enum field. Please check the allowed values.";
        }

        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "INVALID_JSON",
                message,
                "Please check your request body",
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(
            BusinessException ex,
            HttpServletRequest request
    ){
        log.error("Business exception: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                ex.getErrorCode(),
                ex.getMessage(),
                "An error occurred while processing your request",
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ){
        log.error("Resource not found: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                ex.getErrorCode(),
                ex.getMessage(),
                "The requested resource could not be found",
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ){
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = ErrorResponseDTO.of(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                "Please try again later or contact support",
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
