package com.reactivespring.movieinfoservice.controller;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import com.reactivespring.movieinfoservice.service.MovieInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.movieinfoservice.controller
 */
@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

    private final MovieInfoService movieInfoService;

    public MoviesInfoController(MovieInfoService movieInfoService) {
        this.movieInfoService = movieInfoService;
    }
    /*
    curl -i \
    -d '{"movieInfoId":1, "name":"Batman Begins", "year":2005, "cast":["Christian Bale", "Michael Cane"], "release_date":"2005-06-13"}' \
    -H "Content-Type: application-json" \
    -X POST http://localhost:8083/v1/movieInfos
     */

    @PostMapping("/movieInfos")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
        return movieInfoService.addMovie(movieInfo);
    }

    @GetMapping("/movieInfos")
    public Flux<MovieInfo> getAllMovies() {
        return  movieInfoService.getAllMovies();
    }

    @GetMapping(value = "/movieInfos/{id}", produces = "application/json")
    public Mono<MovieInfo> getMovieById(@PathVariable String id) {
        return  movieInfoService.getMovieById(id);
    }

    @PutMapping("/movieInfos/{id}")
    public Mono<MovieInfo> updateInfo(@PathVariable String id ,@RequestBody MovieInfo updated) {
        return movieInfoService.updateInfo(updated, id);
    }

    @DeleteMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteInfo(@PathVariable String id) {
        return movieInfoService.deleteInfo(id);
    }
}
