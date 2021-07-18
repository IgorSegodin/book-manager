package org.isegodin.web.bookmanager.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.WebFilter;

import java.util.regex.Pattern;

/**
 * @author isegodin
 */
@Configuration
public class WebConfig {

    // TODO do we need this filter ?
    @Bean
    public WebFilter spaFilter() {
        final String REGEX = "(?!/actuator|/api|/_nuxt|/static|/index\\.html|/200\\.html|/favicon\\.ico|/sw\\.js).*$";
        Pattern pattern = Pattern.compile(REGEX);

        AntPathMatcher pathMatcher = new AntPathMatcher();

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String fullPath = request.getPath().value();
            String withinPath = request.getPath().pathWithinApplication().value();

            boolean mutate = false;
            String requestPath = null;

            if (pathMatcher.match("/**/spa/**", fullPath)) {
                mutate = true;
                requestPath = fullPath.replace("/spa/", "/");
            }

            if (!mutate) {
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
        };
    }
}
