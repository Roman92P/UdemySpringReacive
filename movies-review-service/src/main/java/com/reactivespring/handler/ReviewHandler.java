package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.repository.ReviewReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.handler
 */
@Component
public class ReviewHandler {

    private final ReviewReactiveRepository repository;

    public ReviewHandler(ReviewReactiveRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return
                request.bodyToMono(Review.class)
                        .flatMap(repository::save)
                        .flatMap(savedReview -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview));
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        Optional<String> movieInfoId = request.queryParam("movieInfoId");

        if(movieInfoId.isPresent()) {
            Flux<Review> flux = repository.findReviewsByMovieInfoId(Long.valueOf(movieInfoId.get()));
            return ServerResponse.ok().body(flux, Review.class);
        } else {
            Flux<Review> all = repository.findAll();
            return ServerResponse.ok().body(all, Review.class);
        }
    }

    public Mono<ServerResponse> updateReview(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Review> byId = repository.findById(id);

        return
                byId.flatMap(review -> request.bodyToMono(Review.class)
                        .map(review1 -> {
                            review.setComment(review1.getComment());
                            review.setRating(review1.getRating());
                            return review;
                        })
                        .flatMap(repository::save)
                        .flatMap(savedReview -> ServerResponse.ok().bodyValue(savedReview)));
    }

    public Mono<ServerResponse> deleteReview(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Review> byId = repository.findById(id);
        return byId.flatMap(review -> repository.deleteById(id))
                .then(ServerResponse.noContent().build());
    }
}
