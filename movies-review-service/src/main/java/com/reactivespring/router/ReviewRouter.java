package com.reactivespring.router;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.router
 */
@Configuration
public class ReviewRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(ReviewHandler reviewHandler) {
        return route()
                .nest(path("/v1/review"), builder -> {
                    builder
                            .POST("", reviewHandler::addReview)
                            .GET("", reviewHandler::getReviews)
                            .PUT("/{id}", reviewHandler::updateReview)
                            .DELETE("/{id}", reviewHandler::deleteReview);
                })
                .build();
    }


}
