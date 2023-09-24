package com.reactivespring.movieinfoservice.repository;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Roman Pashkov created on 23.09.2023 inside the package - com.reactivespring.movieinfoservice.repository
 */
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

    Flux<MovieInfo> findByYear(Integer year);

    Flux<MovieInfo> findByName(String name);
}
