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

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final PasswordEncoder encoder;

    @GetMapping
    public Flux<AppUser> getAllUsers(@RequestHeader("email") String email) {
        return appUserService.getAllUsers();
    }

    @PostMapping
    @CrossOrigin
    public Mono<ResponseEntity<Object>> saveUser(@RequestBody Mono<AppUser> appUserMono, @RequestHeader("email") String email) {
        return appUserMono
//                .doOnSuccess(appUser -> appUser.setPassword(encoder.encode(appUser.getPassword())))
                .doOnSuccess(appUser -> appUser.setPassword(appUser.getName().concat(NanoIdUtils.randomNanoId())))
                .flatMap(appUserService::saveUser)
                .map(savedAppUser -> ResponseEntity.created(URI.create("/user/" + savedAppUser.getId())).build())
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(error.getMessage())))
                .log();
    }

}
