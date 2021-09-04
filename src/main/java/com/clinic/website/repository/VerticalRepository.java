package com.clinic.website.repository;

import com.clinic.website.entities.Vertical;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerticalRepository extends ReactiveMongoRepository<Vertical, Long> {
}
