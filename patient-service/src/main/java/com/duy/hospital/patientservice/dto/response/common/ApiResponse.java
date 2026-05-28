package com.duy.hospital.patientservice.dto.response.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private String code;
    private String message;
    private T data;
    private List<ApiError> errors;

    @Builder.Default
    private Instant timestamp = Instant.now();

    private String traceId;
    private String path;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message("OK")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(201)
                .message("Created")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> noContent() {
        return ApiResponse.<T>builder()
                .success(true)
                .status(204)
                .message("No Content")
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String code, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .code(code)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(int status, String code, String message, List<ApiError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(status)
                .code(code)
                .message(message)
                .errors(errors)
                .build();
    }

    public static <T> ApiResponse<T> of(ResponseCode rc, T data) {
        return ApiResponse.<T>builder()
                .success(rc.getHttpStatus().is2xxSuccessful())
                .status(rc.getStatus())
                .code(rc.getCode())
                .message(rc.getMessage())
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> error(ResponseCode rc) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(rc.getStatus())
                .code(rc.getCode())
                .message(rc.getMessage())
                .build();
    }

    public static <T> ApiResponse<T> error(ResponseCode rc, List<ApiError> errors) {
        return ApiResponse.<T>builder()
                .success(false)
                .status(rc.getStatus())
                .code(rc.getCode())
                .message(rc.getMessage())
                .errors(errors)
                .build();
    }
}

