package com.duy.hospital.apigateway.filter;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    private static final Logger log = LoggerFactory.getLogger(CorrelationIdFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestCorrelationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);
        String correlationId = requestCorrelationId == null || requestCorrelationId.isBlank()
                ? UUID.randomUUID().toString()
                : requestCorrelationId;

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder.header(CORRELATION_ID_HEADER, correlationId))
                .build();

        mutatedExchange.getResponse().beforeCommit(() -> {
            HttpHeaders headers = mutatedExchange.getResponse().getHeaders();
            headers.set(CORRELATION_ID_HEADER, correlationId);
            return Mono.empty();
        });

        long startTime = System.currentTimeMillis();
        String method = mutatedExchange.getRequest().getMethod().name();
        String path = mutatedExchange.getRequest().getURI().getRawPath();

        log.info("gateway request method={} path={} correlationId={}", method, path, correlationId);

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> {
                    int status = mutatedExchange.getResponse().getStatusCode() == null
                            ? 0
                            : mutatedExchange.getResponse().getStatusCode().value();
                    long durationMs = System.currentTimeMillis() - startTime;
                    log.info(
                            "gateway response method={} path={} status={} durationMs={} correlationId={}",
                            method,
                            path,
                            status,
                            durationMs,
                            correlationId
                    );
                });
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
