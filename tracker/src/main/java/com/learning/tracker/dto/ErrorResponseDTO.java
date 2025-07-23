package com.learning.tracker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDTO {

    private String errorCode;
    private String message;
    private String details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private String path;
    private int status;

    public static ErrorResponseDTO of(String errorCode, String message, String path, int status){
        return ErrorResponseDTO.builder()
                .message(message)
                .errorCode(errorCode)
                .path(path)
                .status(status)
                .build();
    }

    public static ErrorResponseDTO of(String errorCode, String message, String details, String path, int status){
        return ErrorResponseDTO.builder()
                .message(message)
                .errorCode(errorCode)
                .path(path)
                .status(status)
                .details(details)
                .build();
    }
}
