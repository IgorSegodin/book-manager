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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

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

        String basePath = Optional.ofNullable(webFluxProperties.getBasePath())
                .orElse("");

        String fullPath = request.getPath().value();


//        Stream.of(
//                (Function<String, Mono<Void>>)(path) -> {
//                    if ( pathMatcher.match("/", fullPath)
//                            || pathMatcher.match(basePath, fullPath)) {
//                        ServerHttpResponse response = exchange.getResponse();
//
//                        response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
//                        response.getHeaders().setLocation(URI.create(
//                                request.getPath().contextPath().value() +
//                                        "/spa"
//                        ));
//
//                        return response.setComplete();
//                    }
//                    return null;
//                },
//
//                ()
//        )


        if (
                pathMatcher.match("/", fullPath)
                        || pathMatcher.match(basePath, fullPath)
        ) {
            ServerHttpResponse response = exchange.getResponse();

            response.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
            response.getHeaders().setLocation(URI.create(
                    request.getPath().contextPath().value() +
                            "/spa"
            ));

            return response.setComplete();

        } else if (pathMatcher.match(basePath + "/spa/**/*.*", fullPath)) {
            return mutateRequestPath(
                    exchange,
                    chain,
                    fullPath.replace("/spa/", "/")
            );
        } else if (pathMatcher.match(basePath + "/spa/**", fullPath)) {
            return mutateRequestPath(
                    exchange,
                    chain,
                    basePath + "/"
            );
        } else {
            return chain.filter(exchange);
        }
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
