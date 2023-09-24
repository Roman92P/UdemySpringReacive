package com.reactivespring.routes;

import com.reactivespring.domain.Review;
import com.reactivespring.handler.ReviewHandler;
import com.reactivespring.repository.ReviewReactiveRepository;
import com.reactivespring.router.ReviewRouter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest
//two beans that will be tested
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class})
@AutoConfigureWebTestClient
public class ReviewsUnitTest {

    @MockBean
    private ReviewReactiveRepository reviewReactiveRepository;

    @Autowired
    private WebTestClient webTestClient;

    static String uri = "/v1/review";

    @Test
    public void addReviewTest_validation() {
        //given
        var awesome_movie = new Review(null, null, "Awesome Movie", -9.0);

        //when
        when(reviewReactiveRepository.save(isA(Review.class))).thenReturn(Mono.just(awesome_movie));

        //then
        webTestClient
                .post()
                .uri(uri)
                .bodyValue(awesome_movie)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
    @Test
    public void addReviewTest() {
        //given
        var awesome_movie = new Review(null, 1L, "Awesome Movie", 9.0);

        //when
        when(reviewReactiveRepository.save(isA(Review.class))).thenReturn(Mono.just(awesome_movie));

        //then
        webTestClient
                .post()
                .uri(uri)
                .bodyValue(awesome_movie)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(Review.class)
                .consumeWith(movieInfoEntityExchangeResult -> {
                    var savedReview = movieInfoEntityExchangeResult.getResponseBody();
                    assertNotNull(savedReview);
                });
    }

    @Test
    public void getAllReviews() {
        //given
        var reviewList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 2L, "Awesome Movie1", 9.0),
                new Review("abc", 3L, "Awesome Movie2", 8.0));
        //when
        when(reviewReactiveRepository.findAll()).thenReturn(Flux.fromIterable(reviewList));
        //then
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(3);
    }

    @Test
    public void updateReview() {
        //given
        Review update = new Review("abc", 3L, "Awesome MovieTEST", 4.0);
        String id = "abc";

        //when
        when(reviewReactiveRepository.findById(isA(String.class))).thenReturn(Mono.just(update));
        when(reviewReactiveRepository.save(isA(Review.class))).thenReturn(Mono.just(update));
        //then
        webTestClient
                .put()
                .uri(uri + "/{id}", id)
                .bodyValue(update)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    Review responseBody = reviewEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertEquals("Awesome MovieTEST", responseBody.getComment());
                });
    }

    @Test
    public void deleteReview() {
        //given
        Review update = new Review("abc", 3L, "Awesome MovieTEST", 4.0);
        String id = "abc";

        //when
        when(reviewReactiveRepository.findById(isA(String.class))).thenReturn(Mono.just(update));
        when(reviewReactiveRepository.deleteById(isA(String.class))).thenReturn(Mono.empty());
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
    public void getByAnotherId() {
        //given
        Long id = 3L;
        var reviewList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 2L, "Awesome Movie1", 9.0),
                new Review("abc", 3L, "Awesome Movie2", 8.0));
        URI uri1 = UriComponentsBuilder.fromUriString(uri)
                .queryParam("movieInfoId", id)
                .buildAndExpand()
                .toUri();
        //when
        when(reviewReactiveRepository.findReviewsByMovieInfoId(isA(Long.class))).thenReturn(
                Flux.fromIterable(reviewList)
        );

        //then
        webTestClient
                .get()
                .uri(uri1)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(3);
    }
}
