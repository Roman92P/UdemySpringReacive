package com.reactivespring.movieinfoservice.repository;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Roman Pashkov created on 23.09.2023 inside the package - com.reactivespring.movieinfoservice.repository
 */
public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {
}
