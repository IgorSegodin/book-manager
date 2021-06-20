package org.isegodin.web.bookmanager.app;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author isegodin
 */
@RequestMapping("/temp")
@RestController
public class TempController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TempData> get() {
        return Mono.just(new TempData("Test data"));
    }

    @Value
    public static class TempData {

        String data;

    }

}
