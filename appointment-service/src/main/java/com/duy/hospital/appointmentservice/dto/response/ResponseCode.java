package com.duy.hospital.appointmentservice.dto.response;

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
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "DEPARTMENT_NOT_FOUND", "Department not found"),
    DOCTOR_NOT_FOUND(HttpStatus.NOT_FOUND, "DOCTOR_NOT_FOUND", "Doctor not found"),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_NOT_FOUND", "Doctor schedule not found"),
    APPOINTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "APPOINTMENT_NOT_FOUND", "Appointment not found"),

    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "Resource conflict"),
    DUPLICATE_DEPARTMENT_NAME(HttpStatus.CONFLICT, "DUPLICATE_DEPARTMENT_NAME", "Department name already exists"),
    DUPLICATE_SCHEDULE(HttpStatus.CONFLICT, "DUPLICATE_SCHEDULE", "Doctor schedule overlaps with existing schedule"),
    SLOT_NOT_AVAILABLE(HttpStatus.CONFLICT, "SLOT_NOT_AVAILABLE", "Requested time slot is not available"),
    APPOINTMENT_CONFLICT(HttpStatus.CONFLICT, "APPOINTMENT_CONFLICT", "Appointment conflicts with an existing booking"),

    INVALID_STATUS_TRANSITION(HttpStatus.UNPROCESSABLE_ENTITY, "INVALID_STATUS_TRANSITION", "Invalid appointment status transition"),
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
