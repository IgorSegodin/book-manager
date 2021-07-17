package org.isegodin.web.bookmanager.system;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.WebFilter;

import java.util.regex.Pattern;

/**
 * @author isegodin
 */
@Configuration
public class WebConfig {

    // TODO do we need this filter ?
    //    @Bean
    public WebFilter spaFilter() {
        final String REGEX = "(?!/actuator|/api|/_nuxt|/static|/index\\.html|/200\\.html|/favicon\\.ico|/sw\\.js).*$";
        Pattern pattern = Pattern.compile(REGEX);

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            String requestUrl = request.getPath().pathWithinApplication().value();

            if (pattern.matcher(requestUrl).matches() && !requestUrl.equals("/")) {
                // Delegate/Forward to `/` if `pattern` matches and it is not `/`
                // Required because of 'mode: history'usage in frontend routing, see README for further details
                return chain.filter(
                        exchange.mutate()
                                .request(
                                        request.mutate()
                                                .path("/").build()
                                )
                                .build()
                );
            } else {
                return chain.filter(exchange);
            }
        };
    }
}
