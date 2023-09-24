package com.reactivespring.repository;

import com.reactivespring.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Roman Pashkov created on 24.09.2023 inside the package - com.reactivespring.repository
 */
public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {
}
