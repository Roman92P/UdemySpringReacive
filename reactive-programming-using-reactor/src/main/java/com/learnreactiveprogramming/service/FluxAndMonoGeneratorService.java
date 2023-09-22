package com.learnreactiveprogramming.service;

import lombok.var;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Random;


public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

       return Flux.fromIterable(List.of("Alex", "Ben", "Chloe" ));

    }

    public Flux<String> namesFlux_flatmap_async(int stringLength) {

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> namesFlux_concatMap(int stringLength) {

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .concatMap(this::splitString_withDelay)
                .log();
    }

    private Flux<String> splitString_withDelay(String s) {
        var split = s.split("");
        var delay = new Random().nextInt(1000);
        return Flux.fromArray(split)
                .delayElements(Duration.ofMillis(delay));

    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(System.out::println);
    }
}
