package com.reactivespring.movieinfoservice.service;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import com.reactivespring.movieinfoservice.repository.MovieInfoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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

    public Flux<MovieInfo> getAllMovies() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> getMovieById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<MovieInfo> updateInfo(MovieInfo updated, String id) {
        return movieInfoRepository.findById(id)
                .flatMap(movieInfo -> {
                    movieInfo.setCast(updated.getCast());
                    movieInfo.setName(updated.getName());
                    movieInfo.setRelease_date(updated.getRelease_date());
                    movieInfo.setYear(updated.getYear());
                    return movieInfoRepository.save(movieInfo);
                });
    }

    public Mono<Void> deleteInfo(String id) {
        return movieInfoRepository.deleteById(id);
    }

    public Flux<MovieInfo> findByYear(Integer year) {
        return movieInfoRepository.findByYear(year);
    }
}
