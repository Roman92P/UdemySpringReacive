package com.reactivespring.movieinfoservice.controller;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import com.reactivespring.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//to avoid collision with running mongoDB
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoControllerTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @Autowired
    WebTestClient webTestClient;

    static String uri = "/v1/movieInfos";

    @BeforeEach
    void setUp() {
        var moviesInfos = List.of(new MovieInfo("abc", "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knieght",
                        2008, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
                new MovieInfo(null, "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );

        movieInfoRepository.saveAll(moviesInfos).blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo() {
        //given
        MovieInfo movieInfo = new MovieInfo("abcd", "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15"));
        //when
        webTestClient
                .post()
                .uri(uri)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody.getMovieInfoId());
                });
        //then
    }

    @Test
    void getAllMoviesInfo() {
        //given
        //when
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
        //then
    }

    @Test
    void getMoviesInfoByIdTest() {
        //given
        var id = "abc";
        //when
        webTestClient
                .get()
                .uri(uri + "/{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    MovieInfo responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                });
        //then
    }

    @Test
    void updateMovieInfo() {
        //given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15"));
        var id = "abc";
        //when
        webTestClient
                .put()
                .uri(uri + "/{id}", id)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody.getMovieInfoId());
                    assertEquals("Batman Begins1", responseBody.getName());
                });
        //then
    }

    @Test
    void deleteInfo() {

        //given
        var id = "abc";

        //when
        webTestClient
                .delete()
                .uri(uri + "/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);

        //then
        webTestClient
                .get()
                .uri(uri + "/{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    assertThat(movieInfoEntityExchangeResult == null);
                });
    }

    @Test
    void updateMovieInfo_notFound() {
        //given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15"));
        var id = "abcyyy";
        //when
        webTestClient
                .put()
                .uri(uri + "/{id}", id)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isNotFound();
        //then
    }

    @Test
    void getMoviesInfoByIdTest_notFound() {
        //given
        var id = "abdc";
        //when
        webTestClient
                .get()
                .uri(uri + "/{id}", id)
                .exchange()
                .expectStatus()
                .isNotFound();
        //then
    }

    @Test
    void getAllMoviesInfo_Year() {
        //given
        URI uri1 = UriComponentsBuilder.fromUriString(uri)
                .queryParam("year", 2005)
                .buildAndExpand()
                .toUri();
        //when
        webTestClient
                .get()
                .uri(uri1)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
        //then
    }

}