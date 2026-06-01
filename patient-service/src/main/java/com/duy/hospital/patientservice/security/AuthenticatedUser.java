package com.duy.hospital.patientservice.security;

import java.util.Set;
import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        String email,
        Set<String> roles
) {
    public boolean hasRole(String role) {
        return roles.contains(role);
    }
}
