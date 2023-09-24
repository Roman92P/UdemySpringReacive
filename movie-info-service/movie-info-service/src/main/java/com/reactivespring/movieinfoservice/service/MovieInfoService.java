package com.reactivespring.movieinfoservice.service;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import com.reactivespring.movieinfoservice.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.movieinfoservice.service
 */
@Service
public class MovieInfoService {


    private final MovieInfoRepository movieInfoRepository;

    public MovieInfoService(MovieInfoRepository movieInfoRepository) {
        this.movieInfoRepository = movieInfoRepository;
    }

    public Mono<MovieInfo> addMovie(MovieInfo movieInfo) {

        return movieInfoRepository.save(movieInfo);
    }
}
