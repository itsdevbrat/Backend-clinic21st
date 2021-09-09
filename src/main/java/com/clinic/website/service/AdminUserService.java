package com.clinic.website.service;

import com.clinic.website.entities.AdminUser;
import com.clinic.website.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    public Mono<AdminUser> getUser(String email) {
        return adminUserRepository.findFirstByEmail(email).log();
    }
}
