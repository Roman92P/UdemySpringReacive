package com.reactivespring.controller;

import com.reactivespring.domain.Movie;
import com.reactivespring.domain.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.controller
 */
@RestController
@RequestMapping("/v1/movies")
public class MoviesController {

    @Autowired
    WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    @GetMapping("/{id}")
    public Mono<MovieInfo> retrieveMovieById(@PathVariable String id) {
        var ulr = moviesInfoUrl.concat("/{id}");
        return webClient
                .get()
                .uri(ulr, id)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .log();
    }
}
