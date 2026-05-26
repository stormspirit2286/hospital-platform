package com.duy.hospital.authservice.exception;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        String code,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
