package com.duy.hospital.patientservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USER_EMAIL_HEADER = "X-User-Email";
    public static final String USER_ROLES_HEADER = "X-User-Roles";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String userIdHeader = request.getHeader(USER_ID_HEADER);

        if (userIdHeader != null && !userIdHeader.isBlank()) {
            UUID userId;
            try {
                userId = UUID.fromString(userIdHeader);
            } catch (IllegalArgumentException ex) {
                filterChain.doFilter(request, response);
                return;
            }
            Set<String> roles = parseRoles(request.getHeader(USER_ROLES_HEADER));
            AuthenticatedUser user = new AuthenticatedUser(
                    userId,
                    request.getHeader(USER_EMAIL_HEADER),
                    roles
            );

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            roles.stream()
                                    .map(role -> "ROLE_" + role)
                                    .map(SimpleGrantedAuthority::new)
                                    .toList()
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Set<String> parseRoles(String rolesHeader) {
        if (rolesHeader == null || rolesHeader.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .filter(role -> !role.isBlank())
                .map(role -> role.startsWith("ROLE_") ? role.substring("ROLE_".length()) : role)
                .collect(Collectors.toUnmodifiableSet());
    }
}
