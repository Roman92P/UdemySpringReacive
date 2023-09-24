package com.reactivespring.routes;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class ReviewsIntgTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ReviewReactiveRepository repository;

    static String uri = "/v1/review";

    @BeforeEach
    void setUp() {
        var reviewList = List.of(
                new Review(null, 1L, "Awesome Movie", 9.0),
                new Review(null, 2L, "Awesome Movie1", 9.0),
                new Review("abc", 3L, "Awesome Movie2", 8.0));
        repository.saveAll(reviewList).blockLast();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll().block();
    }

    @Test
    public void addReview() {
        //given
        Review awesome_movie = new Review(null, 1L, "Awesome Movie", 9.0);
        //when
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
                    assertNotNull(savedReview.getReviewId());
                });
        //then
        //then
    }

    @Test
    public void getAllReviews() {
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
    public void update() {
        //given
        Review updated = new Review("abc", 1L, "Awesome Movie1", 5.0);
        //when
        webTestClient
                .put()
                .uri(uri + "/abc")
                .bodyValue(updated)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(Review.class)
                .consumeWith(reviewEntityExchangeResult -> {
                    Review responseBody = reviewEntityExchangeResult.getResponseBody();
                    assert responseBody != null;
                    assertEquals("Awesome Movie1", responseBody.getComment());
                });
        //then
    }

    @Test
    public void deleteReview() {
        //given
        String id = "abc";
        //when
        webTestClient
                .delete()
                .uri(uri + "/abc")
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody(Void.class);
        //then
        webTestClient
                .get()
                .uri(uri)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(2);

    }

    @Test
    public void getReviewBymovieInfoId() {
        //given
        Long id = 3L;
        URI uri1 = UriComponentsBuilder.fromUriString(uri)
                .queryParam("movieInfoId", id)
                .buildAndExpand()
                .toUri();
        //when
        webTestClient
                .get()
                .uri(uri1)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(Review.class)
                .hasSize(1);

        //then
    }
}
