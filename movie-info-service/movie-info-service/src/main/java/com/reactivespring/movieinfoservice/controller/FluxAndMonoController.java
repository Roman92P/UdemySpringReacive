package com.reactivespring.movieinfoservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Roman Pashkov created on 23.09.2023 inside the package - com.reactivespring.movieinfoservice.controller
 */
@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<Integer> flux() {
        return Flux.just(1,2,3)
                //print all events behind the scenes
                .log();
    }

    //curl "http://localhost:8083/mono"
    @GetMapping("/mono")
    public Mono<String> helloWorldMono() {
        return Mono.just("hello-world")
                .log();
    }
}
