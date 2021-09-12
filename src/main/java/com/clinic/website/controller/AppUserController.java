package com.clinic.website.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.clinic.website.dto.AuthRequestDTO;
import com.clinic.website.entities.AppUser;
import com.clinic.website.service.AppUserService;
import com.clinic.website.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AppUserController {

    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;
//    private final PasswordEncoder encoder;

    @GetMapping("/page")
    public Flux<AppUser> getUsers(@RequestParam("page") int page) {
        return appUserService.getUsers(page);
    }

    @GetMapping
    public Mono<AppUser> getUser(@RequestHeader("email") String email) {
        return appUserService.getUser(email);
    }

    @GetMapping("/search")
    public Flux<AppUser> searchUsers(@RequestParam("query") String query) {
        return appUserService.searchUsers(query);
    }


    @PutMapping
    public Mono<AppUser> updateUser(@RequestBody AppUser user) {
        return appUserService.updateUser(user);
    }

    @PostMapping
    public Mono<ResponseEntity<String>> saveUser(@RequestBody Mono<AppUser> appUserMono, String email) {
        return appUserMono
//                .doOnSuccess(appUser -> appUser.setPassword(encoder.encode(appUser.getPassword())))
                .doOnSuccess(appUser -> {
                    appUser.setPassword(appUser.getUserName().concat(NanoIdUtils.randomNanoId(
                            new Random(),
                            new char[]{'0', '1', '2', '3', '4'},
                            5)));
                    appUser.setCreatedDate(Instant.now());
                    appUser.setEnabled(true);
                })
                .subscribeOn(Schedulers.parallel())
                .flatMap(appUserService::saveUser)
                .map(savedAppUser -> ResponseEntity.created(URI.create("/user/" + savedAppUser.getId())).body("User saved successfully"))
                .onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(error.getMessage())))
                .log();
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody AuthRequestDTO authRequestDTO) {
        return appUserService
                .getUser(authRequestDTO.getEmail())
                .map(appUser ->
//                        encoder.matches(authRequestDTO.getPassword(), appUser.getPassword())
                        authRequestDTO.getPassword().equals(appUser.getPassword())
                                ? ResponseEntity.ok(jwtUtil.generateToken(appUser.getEmail()))
                                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong username or password"))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such user"))
                .log();
    }

    @GetMapping("/verticals")
    public Mono<List<String>> getUserVerticals(@RequestParam("email") String email ) {
        return appUserService.getUserVerticals(email);
    }

}
