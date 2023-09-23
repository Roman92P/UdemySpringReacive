package com.learnreactiveprogramming.service;

import lombok.var;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

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

    @Test
    void namesMono_flatMapMany() {

        //given
        int stringLengh = 3;

        //when
        var value = fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLengh);

        //them
        StepVerifier.create(value)
                .expectNext("A","L","E","X")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        //given
        int stringLength = 3;

        //when
        var namesFlux = fluxAndMonoGeneratorService.namesFlux_transform(stringLength);

        //then
        StepVerifier.create(namesFlux)
                .expectNext("A","L","E","X", "C","H","L","O","E")
                .verifyComplete();
    }
}