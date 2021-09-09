package com.clinic.website.repository;

import com.clinic.website.entities.AdminUser;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AdminUserRepository extends ReactiveMongoRepository<AdminUser, Long> {

    Mono<AdminUser> findFirstByEmail(String email);
}
