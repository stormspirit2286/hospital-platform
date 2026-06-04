package com.duy.hospital.appointmentservice.exception;

import com.duy.hospital.appointmentservice.dto.response.ApiError;
import com.duy.hospital.appointmentservice.dto.response.ApiResponse;
import com.duy.hospital.appointmentservice.dto.response.ResponseCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrity(DataIntegrityViolationException ex) {
        log.warn("DataIntegrityViolationException: {}", ex.getMostSpecificCause().getMessage());
        ResponseCode rc = ResponseCode.CONFLICT;
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ResponseCode rc = ResponseCode.INVALID_PARAMETER;
        String message = "Invalid value '" + ex.getValue() + "' for parameter '" + ex.getName() + "'";
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc.getStatus(), rc.getCode(), message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        ResponseCode rc = ResponseCode.INVALID_PARAMETER;
        return ResponseEntity
                .status(rc.getStatus())
                .body(ApiResponse.error(rc.getStatus(), rc.getCode(), ex.getMessage()));
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
