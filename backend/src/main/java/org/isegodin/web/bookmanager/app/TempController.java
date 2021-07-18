package org.isegodin.web.bookmanager.app;

import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author isegodin
 */
@RequestMapping("/api/temp")
@RestController
public class TempController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TempData> get() {
        return Mono.just(new TempData("Test data"));
    }

    @Value
    public static class TempData {

        String data;

    }

}
