package com.reactivespring.client;

import com.reactivespring.domain.MovieInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.client
 */
@Component
public class MoviesInfoRestClient {

    @Autowired
    WebClient webClient;

    @Value("${restClient.moviesInfoUrl}")
    private String moviesInfoUrl;

    @GetMapping("/{id}")
    public Mono<MovieInfo> retrieveMovieInfos(@PathVariable String id) {
        var ulr = moviesInfoUrl.concat("/{id}");
        return webClient
                .get()
                .uri(ulr, id)
                .retrieve()
                .bodyToMono(MovieInfo.class)
                .log();
    }
}
