package org.isegodin.web.bookmanager.system.web;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Mutates request url in a way, that is compatible with SPA router.
 * <p>
 * Only paths, which contains '/spa/' will be affected.
 * <p>
 * All router links (without extension) will return same 'index.html' page.
 * <p>
 * Links, which contain extension, will be handled by static resource handler.
 *
 * @author isegodin
 */
@Configuration
@RequiredArgsConstructor
public class SpaWebFilter implements WebFilter {

    private static final String SPA_PATH = "/spa";

    private final WebFluxProperties webFluxProperties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String basePath = Optional.ofNullable(webFluxProperties.getBasePath())
                .orElse("");

        String fullPath = request.getPath().value();

        return Stream.of(

                handleIndexPage(basePath, fullPath),

                handleResource(basePath, fullPath),

                handleSpaPage(basePath, fullPath)

        )
                .filter(Objects::nonNull)
                .findFirst()
                .map(func -> func.apply(exchange, chain))
                .orElseGet(() -> chain.filter(exchange));
    }

    private BiFunction<ServerWebExchange, WebFilterChain, Mono<Void>> handleIndexPage(String basePath, String fullPath) {
        if (!pathMatcher.match("/", fullPath)
                && !pathMatcher.match(basePath, fullPath)) {
            return null;
        }

        return (exchange, chain) -> {
            ServerHttpResponse response = exchange.getResponse();

            response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
            response.getHeaders().setLocation(URI.create(
                    exchange.getRequest().getPath().contextPath().value() +
                            SPA_PATH
            ));

            return response.setComplete();
        };
    }

    private BiFunction<ServerWebExchange, WebFilterChain, Mono<Void>> handleResource(String basePath, String fullPath) {
        if (!pathMatcher.match(basePath + SPA_PATH + "/**/*.*", fullPath)) {
            return null;
        }

        return (exchange, chain) -> mutateRequestPath(
                exchange,
                chain,
                fullPath.replace(SPA_PATH + "/", "/")
        );
    }

    private BiFunction<ServerWebExchange, WebFilterChain, Mono<Void>> handleSpaPage(String basePath, String fullPath) {
        if (!pathMatcher.match(basePath + SPA_PATH + "/**", fullPath)) {
            return null;
        }

        return (exchange, chain) -> mutateRequestPath(
                exchange,
                chain,
                basePath + "/"
        );
    }

    private Mono<Void> mutateRequestPath(ServerWebExchange exchange, WebFilterChain chain, String requestPath) {
        return chain.filter(
                exchange.mutate()
                        .request(
                                exchange.getRequest().mutate()
                                        .path(requestPath)
                                        .build()
                        )
                        .build()
        );
    }
}
