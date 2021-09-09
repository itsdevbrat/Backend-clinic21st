package com.clinic.website.controller;

import com.clinic.website.dto.AuthRequestDTO;
import com.clinic.website.service.AdminUserService;
import com.clinic.website.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    //    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AdminUserService adminUserService;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody AuthRequestDTO authRequestDTO) {
        return adminUserService
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
