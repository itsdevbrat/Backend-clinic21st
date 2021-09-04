package com.clinic.website.service;

import com.clinic.website.entities.AppUser;
import com.clinic.website.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public Flux<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Mono<AppUser> saveUser(AppUser appUser) {
        return getUser(appUser.getEmail())
                .flatMap(user -> Mono.error(new IllegalArgumentException("User Exists with email " + user.getEmail())))
                .switchIfEmpty(Mono.defer(() -> appUserRepository.save(appUser))).cast(AppUser.class).log();
    }

    public Mono<AppUser> getUser(String email) {
        return appUserRepository.findFirstByEmail(email);
    }
}
