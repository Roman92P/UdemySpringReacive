package com.reactivespring.controller;

import com.reactivespring.client.MoviesInfoRestClient;
import com.reactivespring.client.ReviewsRestClient;
import com.reactivespring.domain.Movie;
import com.reactivespring.domain.Review;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.controller
 */
@RestController
@RequestMapping("/v1/movies")
public class MoviesController {

    private final MoviesInfoRestClient moviesInfoRestClient;

    private final ReviewsRestClient reviewsRestClient;

    public MoviesController(MoviesInfoRestClient moviesInfoRestClient, ReviewsRestClient reviewsRestClient) {
        this.moviesInfoRestClient = moviesInfoRestClient;
        this.reviewsRestClient = reviewsRestClient;
    }

    //curl -i "http://localhost:8085/v1/movies/1"
    @GetMapping("/{id}")
    public Mono<Movie> retrieveMovieById(@PathVariable("id") String movieId) {

        return moviesInfoRestClient.retrieveMovieInfos(movieId)
                .flatMap(movieInfo -> {
                    Mono<List<Review>> listMono = reviewsRestClient.retrieveReviews(movieId).collectList();
                    return listMono.map(reviews -> new Movie(movieInfo, reviews));
                });
    }
}
