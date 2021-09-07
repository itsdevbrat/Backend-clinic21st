package com.clinic.website.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.clinic.website.entities.AppUser;
import com.clinic.website.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Instant;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder encoder;

    @GetMapping
    public Flux<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @PostMapping
    @CrossOrigin
    public Mono<ResponseEntity<String>> saveUser(@RequestBody Mono<AppUser> appUserMono, String email) {
        return appUserMono
//                .doOnSuccess(appUser -> appUser.setPassword(encoder.encode(appUser.getPassword())))
                .doOnSuccess(appUser -> {
                    appUser.setPassword(appUser.getUserName().concat(NanoIdUtils.randomNanoId(
                            new Random(),
                            new char[]{'0', '1', '2', '3', '4'},
                            5)));
                    appUser.setCreatedDate(Instant.now());
                })
                .subscribeOn(Schedulers.parallel())
                .flatMap(appUserService::saveUser)
                .map(savedAppUser -> ResponseEntity.created(URI.create("/user/" + savedAppUser.getId())).body("User saved successfully"))
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(error.getMessage())))
                .log();
    }

}
