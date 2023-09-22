package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
    @Test
    void namesFlux_concatMap() {
        //given
        int stringLength = 3;

        //when
        Flux<String> stringFlux = fluxAndMonoGeneratorService.namesFlux_concatMap(3);

        //then
        StepVerifier.create(stringFlux)
                .expectNext("A","L","E","X", "C","H","L","O","E")
                .verifyComplete();
    }
}