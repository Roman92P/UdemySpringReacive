package com.reactivespring.movieinfoservice.repository;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//this will scan project in look for repositories and make them available in test
@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    public void setUp() {

        var moviesInfos = List.of(new MovieInfo("abc", "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knieght",
                        2008, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
                new MovieInfo(null, "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
                );

        //blocking calls are only available in test cases, because you are blocking the thread
        movieInfoRepository.saveAll(moviesInfos).blockLast();
    }

    @AfterEach
    public void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAllTest() {

        //given

        //when
        var movieInfoFlux = movieInfoRepository.findAll();

        //then
        StepVerifier.create(movieInfoFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findByIdTest() {

        //given

        //when
        var movieInfoFlux = movieInfoRepository.findById("abc");

        //then
        StepVerifier.create(movieInfoFlux)
                .assertNext(movieInfo -> {
                    assertEquals("Batman Begins", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveTest() {

        //given

        //when
        var movieInfoFlux = movieInfoRepository.save(new MovieInfo(null, "Dark Knight Rises1",
                2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        //then
        StepVerifier.create(movieInfoFlux)
                .assertNext(movieInfo -> {
                    assertEquals("Dark Knight Rises1", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateTest() {

        //given
        var movieInfo = movieInfoRepository.findById("abc").block();
        movieInfo.setYear(2022);

        //when
        var movieInfoMono = movieInfoRepository.save(movieInfo).log();

        //then
        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo1 -> {
                    assertNotNull(movieInfo1.getMovieInfoId());
                    assertEquals("Batman Begins", movieInfo1.getName());
                })
                .verifyComplete();
    }

    @Test
    void deleteTest() {

        //given

        //when
        var movieInfoFlux = movieInfoRepository.deleteById("abc").block();
        var allMovies = movieInfoRepository.findAll().log();

        //then
        StepVerifier.create(allMovies)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void findByYear() {

        //given

        //when
        var allMovies = movieInfoRepository.findByYear(2005).log();

        //then
        StepVerifier.create(allMovies)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void findByName() {

        //given

        //when
        var allMovies = movieInfoRepository.findByName("Dark Knight Rises").log();

        //then
        StepVerifier.create(allMovies)
                .expectNextCount(1)
                .verifyComplete();
    }

}