package com.duy.hospital.authservice.dto.response;

import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID userId,
        String email,
        String fullName,
        String phone,
        String status,
        List<String> roles
) {
}
