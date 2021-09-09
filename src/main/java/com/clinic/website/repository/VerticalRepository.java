package com.clinic.website.repository;

import com.clinic.website.entities.Vertical;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface VerticalRepository extends ReactiveMongoRepository<Vertical, Long> {
    Mono<Vertical> findByVerticalName(String vertical);
}
