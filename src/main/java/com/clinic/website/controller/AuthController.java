package com.clinic.website.controller;

import com.clinic.website.dto.AuthRequestDTO;
import com.clinic.website.service.AppUserService;
import com.clinic.website.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;
//    private final PasswordEncoder encoder;

    @PostMapping("/login")
    @CrossOrigin
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

}
