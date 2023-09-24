package com.reactivespring.router;

import com.reactivespring.handler.ReviewHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.router
 */
@Configuration
public class RevieRouter {

    @Bean
    public RouterFunction<ServerResponse> reviewRoute(ReviewHandler reviewHandler) {
        return route()
                .POST("/v1/rewievs", request ->  reviewHandler.addReview(request))
                .build();
    }
}
