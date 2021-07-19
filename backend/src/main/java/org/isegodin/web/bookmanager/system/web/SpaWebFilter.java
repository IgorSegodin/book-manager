package org.isegodin.web.bookmanager.system.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author isegodin
 */
@Configuration
@RequiredArgsConstructor
public class SpaWebFilter implements WebFilter {

    private final WebFluxProperties webFluxProperties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String basePath = webFluxProperties.getBasePath();

        String fullPath = request.getPath().value();

        String requestPath = null;

        if (basePath != null && pathMatcher.match(basePath, fullPath)) {
            requestPath = basePath + "/";
        } else if (pathMatcher.match("/**/spa/**", fullPath)) {
            requestPath = fullPath.replace("/spa/", "/");
        }

        if (requestPath == null) {
            return chain.filter(exchange);
        }

        return chain.filter(
                exchange.mutate()
                        .request(
                                request.mutate()
                                        .path(requestPath)
                                        .build()
                        )
                        .build()
        );
    }
}
