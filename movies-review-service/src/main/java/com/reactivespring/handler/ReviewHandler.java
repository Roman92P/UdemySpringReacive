package com.reactivespring.handler;

import com.reactivespring.domain.Review;
import com.reactivespring.exception.ReviewDataException;
import com.reactivespring.repository.ReviewReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.handler
 */
@Component
@Slf4j
public class ReviewHandler {

    @Autowired
    private Validator validator;
    private final ReviewReactiveRepository repository;

    public ReviewHandler(ReviewReactiveRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> addReview(ServerRequest request) {
        return
                request.bodyToMono(Review.class)
                        .doOnNext(this::validate)
                        .flatMap(repository::save)
                        .flatMap(savedReview -> ServerResponse.status(HttpStatus.CREATED).bodyValue(savedReview));
    }

    private void validate(Review review) {
        var vialations = validator.validate(review);
        log.info("ConstraintViolations: {}", vialations);
        if (!vialations.isEmpty()) {
            var errorMessage = vialations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(","));
            throw new ReviewDataException(errorMessage);
        }
    }

    public Mono<ServerResponse> getReviews(ServerRequest request) {

        Optional<String> movieInfoId = request.queryParam("movieInfoId");

        if (movieInfoId.isPresent()) {
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
