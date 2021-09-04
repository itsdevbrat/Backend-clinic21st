package com.clinic.website.repository;

import com.clinic.website.entities.AppUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AppUserRepository extends ReactiveMongoRepository<AppUser, Long> {

    Mono<AppUser> findFirstByEmail(String email);
}
