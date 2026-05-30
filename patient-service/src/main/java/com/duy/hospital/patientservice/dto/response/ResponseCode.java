package com.duy.hospital.patientservice.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {

    SUCCESS(HttpStatus.OK, "SUCCESS", "Success"),
    CREATED(HttpStatus.CREATED, "CREATED", "Resource created successfully"),
    NO_CONTENT(HttpStatus.NO_CONTENT, "NO_CONTENT", "No content"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Bad request"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Validation failed"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "INVALID_PARAMETER", "Invalid parameter"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Unauthorized"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "Invalid or expired token"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "Access denied"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found"),
    PATIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PATIENT_NOT_FOUND", "Patient not found"),
    INSURANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "INSURANCE_NOT_FOUND", "Insurance not found"),
    EMERGENCY_CONTACT_NOT_FOUND(HttpStatus.NOT_FOUND, "EMERGENCY_CONTACT_NOT_FOUND", "Emergency contact not found"),

    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "Resource conflict"),
    PATIENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "PATIENT_ALREADY_EXISTS", "Patient already exists"),
    DUPLICATE_PHONE(HttpStatus.CONFLICT, "DUPLICATE_PHONE", "Phone number already registered"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "Email already registered"),

    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_CONTENT, "UNPROCESSABLE_ENTITY", "Unprocessable entity"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Internal server error"),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE", "Service temporarily unavailable");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public int getStatus() {
        return httpStatus.value();
    }
}


