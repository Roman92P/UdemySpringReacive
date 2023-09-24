package com.reactivespring.movieinfoservice.controller;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import com.reactivespring.movieinfoservice.service.MovieInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.movieinfoservice.controller
 */
@RestController
@RequestMapping("/v1")
@Slf4j
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
    public Flux<MovieInfo> getAllMovies(@RequestParam(value = "year", required = false) Integer year) {

        if (year != null) {
            log.info("Year is: {}", year);
           return movieInfoService.findByYear(year);
        }
        return  movieInfoService.getAllMovies();
    }

    @GetMapping(value = "/movieInfos/{id}", produces = "application/json")
    public Mono<ResponseEntity<MovieInfo>> getMovieById(@PathVariable String id) {

        return  movieInfoService.getMovieById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PutMapping("/movieInfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateInfo(@PathVariable String id , @RequestBody MovieInfo updated) {
        return movieInfoService.updateInfo(updated, id)/*.log()*/
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteInfo(@PathVariable String id) {
        return movieInfoService.deleteInfo(id);
    }


}
