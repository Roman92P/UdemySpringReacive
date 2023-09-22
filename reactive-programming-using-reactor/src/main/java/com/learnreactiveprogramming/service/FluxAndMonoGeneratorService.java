package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;

import java.util.List;


public class FluxAndMonoGeneratorService {

    public Flux<String> namesFlux() {

       return Flux.fromIterable(List.of("Alex", "Ben", "Chloe" ));

    }
    public static void main(String[] args) {

        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(System.out::println);
    }
}
