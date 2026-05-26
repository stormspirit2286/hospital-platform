package com.duy.hospital.apigateway.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/login",
            "/api/auth/refresh",
            "/actuator/health",
            "/actuator/info"
    };

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter
    ) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .pathMatchers("/api/auth/logout", "/api/auth/me").authenticated()
                        .pathMatchers("/api/patients/**").authenticated()
                        .pathMatchers("/api/appointments/**").authenticated()
                        .pathMatchers("/api/departments/**").authenticated()
                        .pathMatchers("/api/doctors/**").authenticated()
                        .anyExchange().denyAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                )
                .build();
    }

    @Bean
    Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = extractRoles(jwt).stream()
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();

            return Mono.just(new JwtAuthenticationToken(jwt, authorities, jwt.getSubject()));
        };
    }

    private List<String> extractRoles(Jwt jwt) {
        Object roles = jwt.getClaims().get("roles");
        if (roles instanceof Collection<?> collection) {
            return collection.stream()
                    .flatMap(SecurityConfig::toRoleStream)
                    .distinct()
                    .sorted()
                    .toList();
        }
        return toRoleStream(roles)
                .distinct()
                .sorted()
                .toList();
    }

    private static Stream<String> toRoleStream(Object value) {
        if (value == null) {
            return Stream.empty();
        }
        return Stream.of(value.toString().split(","))
                .map(String::trim)
                .filter(role -> !role.isBlank());
    }
}
