package com.duy.hospital.apigateway.filter;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class IdentityForwardingFilter implements GlobalFilter, Ordered {

    public static final String USER_ID_HEADER = "X-User-Id";
    public static final String USER_EMAIL_HEADER = "X-User-Email";
    public static final String USER_ROLES_HEADER = "X-User-Roles";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerWebExchange sanitizedExchange = removeIdentityHeaders(exchange);

        return sanitizedExchange.getPrincipal()
                .filter(this::isAuthenticatedJwt)
                .cast(JwtAuthenticationToken.class)
                .map(authentication -> addIdentityHeaders(sanitizedExchange, authentication))
                .defaultIfEmpty(sanitizedExchange)
                .flatMap(chain::filter);
    }

    private boolean isAuthenticatedJwt(Principal principal) {
        return principal instanceof Authentication authentication
                && authentication.isAuthenticated()
                && principal instanceof JwtAuthenticationToken;
    }

    private ServerWebExchange removeIdentityHeaders(ServerWebExchange exchange) {
        return exchange.mutate()
                .request(builder -> builder.headers(headers -> {
                    headers.remove(USER_ID_HEADER);
                    headers.remove(USER_EMAIL_HEADER);
                    headers.remove(USER_ROLES_HEADER);
                }))
                .build();
    }

    private ServerWebExchange addIdentityHeaders(
            ServerWebExchange exchange,
            JwtAuthenticationToken authentication
    ) {
        Jwt jwt = authentication.getToken();
        String email = jwt.getClaimAsString("email");
        List<String> roles = jwt.getClaimAsStringList("roles");

        return exchange.mutate()
                .request(builder -> builder.headers(headers -> {
                    headers.set(USER_ID_HEADER, jwt.getSubject());
                    if (email != null && !email.isBlank()) {
                        headers.set(USER_EMAIL_HEADER, email);
                    }
                    if (roles != null && !roles.isEmpty()) {
                        headers.set(USER_ROLES_HEADER, roles.stream()
                                .sorted()
                                .collect(Collectors.joining(",")));
                    }
                }))
                .build();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 20;
    }
}
