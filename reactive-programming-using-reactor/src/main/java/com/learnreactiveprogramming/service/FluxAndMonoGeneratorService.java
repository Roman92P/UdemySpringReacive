package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;


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
//        var delay = new Random().nextInt(1000);
        var delay = 1000;
        return Flux.fromArray(split)
                .delayElements(Duration.ofMillis(delay));

    }

    public Mono<String> namesMono_map_filter(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitStringMono);
    }

    public Flux<String> namesMono_flatMapMany(int stringLength) {
        return Mono.just("alex")
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMapMany(this::splitString);
    }

    private Flux<String> splitString(String s) {
        var arr = s.split("");
        return Flux.fromArray(arr);
    }

    private Mono<List<String>> splitStringMono(String s) {
        var split = s.split("");
        var split1 = List.of(split);
        return Mono.just(split1);
    }

    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(System.out::println);
    }

    public Flux<String> namesFlux_flatMap(int stringLength) {

        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .map(String::toUpperCase)
                .filter(s -> s.length() > stringLength)
                .flatMap(this::splitString_withDelay)
                .log();
    }


    public Flux<String> namesFlux_transform(int stringLength) {
        var filterMap = (Function<Flux<String>, Flux<String>>) name -> name.map(String::toUpperCase)
                .filter(s -> s.length() > stringLength);
        return Flux.fromIterable(List.of("alex", "ben", "chloe"))
                .transform(filterMap)
                .flatMap(this::splitString_withDelay)
                .log();
    }

    public Flux<String> explore_zip() {
        var abcFlux = Flux.just("A","B","C");
        var defFlux = Flux.just("D","E","F");
        return Flux.zip(abcFlux, defFlux, (first, second) -> first + second);
    }
}
