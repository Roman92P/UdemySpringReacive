package com.reactivespring.movieinfoservice.controller;

import com.reactivespring.movieinfoservice.domain.MovieInfo;
import com.reactivespring.movieinfoservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;


@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
class MoviesInfoControllerUnitTest {

    @Autowired
    WebTestClient webTestClient;

    //injecting spring bean into Spring Context
    @MockBean
    private MovieInfoService movieInfoServiceMock;

    static String uri = "/v1/movieInfos";


    @Test
    void addMovieInfo() {

        //given
        MovieInfo movieInfo = new MovieInfo("abcd", "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15"));
        //when
        when(movieInfoServiceMock.addMovie(isA(MovieInfo.class))).thenReturn(Mono.just(
                new MovieInfo("mockId", "Batman Begins1",
                        2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15"))
        ));
        //then
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
                    assertEquals("mockId", responseBody.getMovieInfoId());
                });
    }

    @Test
    void getAllMovies() {

        //given
        var moviesInfos = List.of(new MovieInfo("abc", "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knieght",
                        2008, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
                new MovieInfo(null, "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );


        //when
        when(movieInfoServiceMock.getAllMovies())
                .thenReturn(Flux.fromIterable(moviesInfos));

        //then
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);

    }

    @Test
    void getMovieById() {

        //given
        var moviesInfos = List.of(new MovieInfo("abc", "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knieght",
                        2008, List.of("Christian Bale", "Heath Ledger"), LocalDate.parse("2008-07-18")),
                new MovieInfo(null, "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        );


        //when
        when(movieInfoServiceMock.getMovieById(any()))
                .thenReturn(Mono.just(moviesInfos.get(0)));

        //then
        webTestClient
                .get()
                .uri(uri+"/{id}", "abc")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertEquals("Batman Begins", responseBody.getName());
                });
    }

    @Test
    void updateInfo() {
        //given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15"));
        var id = "abc";
        //when
        when(movieInfoServiceMock.updateInfo(isA(MovieInfo.class), any())).thenReturn(
                Mono.just(new MovieInfo("test", "Batman Begins1",
                        2005, List.of("Christian Bale", "Michael Kane"), LocalDate.parse("2005-06-15"))
        ));
        //then
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
                    assert responseBody != null;
                    assertNotNull(responseBody.getMovieInfoId());
                    assertEquals("Batman Begins1", responseBody.getName());
                });
    }

    @Test
    void deleteInfo() {
        //given
        var id = "abc";

        //when
        when(movieInfoServiceMock.deleteInfo(isA(String.class))).thenReturn(Mono.empty());

        //then
        webTestClient
                .delete()
                .uri(uri + "/{id}", id)
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
    }

    @Test
    void addMovieInfo_validation() {

        //given
        MovieInfo movieInfo = new MovieInfo("abcd", "",
                -2005, List.of(""), LocalDate.parse("2005-06-15"));
        //when

        //then
        webTestClient
                .post()
                .uri(uri)
                .bodyValue(movieInfo)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(String.class)
                .consumeWith(stringEntityExchangeResult -> {
                    String responseBody = stringEntityExchangeResult.getResponseBody();
                    assertNotNull(responseBody);
                    var expectedError = "Cast must be present,Name must be present,Year must be a Positive value";
                    assertEquals(expectedError, responseBody);
                });
//                .expectBody(MovieInfo.class)
//                .consumeWith(movieInfoEntityExchangeResult -> {
//                    var responseBody = movieInfoEntityExchangeResult.getResponseBody();
//                    assertNotNull(responseBody.getMovieInfoId());
//                    assertEquals("mockId", responseBody.getMovieInfoId());
//                });
    }
}