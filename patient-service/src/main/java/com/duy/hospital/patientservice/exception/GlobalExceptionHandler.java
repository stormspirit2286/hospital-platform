package com.duy.hospital.patientservice.exception;

import com.duy.hospital.patientservice.dto.response.ApiError;
import com.duy.hospital.patientservice.dto.response.ApiResponse;
import com.duy.hospital.patientservice.dto.response.ResponseCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex) {
        ResponseCode rc = ex.getResponseCode();
        log.warn("AppException: code={} message={}", rc.getCode(), ex.getMessage());
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc.getStatus(), rc.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toApiError)
                .toList();
        List<String> failedFields = errors.stream().map(ApiError::getField).toList();
        log.warn("Validation failed: {} error(s) on fields {}", errors.size(), failedFields);
        ResponseCode rc = ResponseCode.VALIDATION_FAILED;
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc, errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiError> errors = ex.getConstraintViolations().stream()
                .map(v -> ApiError.builder()
                        .field(v.getPropertyPath().toString())
                        .code(v.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName())
                        .message(v.getMessage())
                        .rejectedValue(v.getInvalidValue())
                        .build())
                .toList();
        ResponseCode rc = ResponseCode.VALIDATION_FAILED;
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc, errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        ResponseCode rc = ResponseCode.BAD_REQUEST;
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc.getStatus(), rc.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex) {
        log.error("Unhandled exception", ex);
        ResponseCode rc = ResponseCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc));
    }

    private ApiError toApiError(FieldError fieldError) {
        return ApiError.builder()
                .field(fieldError.getField())
                .code(fieldError.getCode())
                .message(fieldError.getDefaultMessage())
                .rejectedValue(fieldError.getRejectedValue())
                .build();
    }
}
