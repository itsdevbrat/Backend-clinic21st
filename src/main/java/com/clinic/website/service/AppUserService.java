package com.clinic.website.service;

import com.clinic.website.entities.AppUser;
import com.clinic.website.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private static final int PAGE_SIZE = 15;
    private final AppUserRepository appUserRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    public Flux<AppUser> getUsers(int page) {
        return appUserRepository
                .findAll()
                .sort(Comparator.comparing(AppUser::getCreatedDate).reversed())
                .skip((long) page * PAGE_SIZE)
                .take(PAGE_SIZE);
    }

    public Mono<AppUser> saveUser(AppUser appUser) {
        return getUser(appUser.getEmail())
                .flatMap(user -> Mono.<AppUser>error(new IllegalArgumentException("User already exists with email " + user.getEmail())))
                .switchIfEmpty(Mono.defer(() -> appUserRepository.save(appUser))).log();
    }

    public Mono<AppUser> getUser(String email) {
        return appUserRepository.findFirstByEmail(email).log();
    }

    public Flux<AppUser> searchUsers(String query) {
        return mongoTemplate.find(
                Query.query(Criteria.where("enabled").is(false))
                        .addCriteria(TextCriteria.forDefaultLanguage()
                                .matching(query)),
                AppUser.class);
    }

    public Mono<AppUser> updateUser(AppUser appUser) {
        return appUserRepository.findFirstByEmail(appUser.getEmail())
                .flatMap(appUserRepository::save)
                .log();
    }
}
