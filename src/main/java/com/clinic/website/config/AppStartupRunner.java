package com.clinic.website.config;

import com.clinic.website.entities.AdminUser;
import com.clinic.website.entities.AppUser;
import com.clinic.website.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppStartupRunner implements CommandLineRunner {

    private final ReactiveMongoTemplate mongoTemplate;
    private final AdminUserRepository adminUserRepository;


    @Override
    public void run(String... args) throws Exception {
        TextIndexDefinition textIndex = new TextIndexDefinition.TextIndexDefinitionBuilder()
                .onField("userName", 3f)
                .onField("email", 2f)
                .onField("phoneNumber")
                .build();
        mongoTemplate.indexOps(AppUser.class).ensureIndex(textIndex).subscribe(log::info)   ;
        AdminUser adminUser = new AdminUser();
        adminUser.setEnabled(true);
        adminUser.setUserName("Dev");
        adminUser.setEmail("devbratdash1998@gmail.com");
        adminUser.setPassword("dev1998");
        adminUser.setCreatedDate(Instant.now());
        log.info("Saving................../");
        adminUserRepository.save(adminUser)
                .subscribe(user -> log.info("{} {}",user.getPassword(), user.getEmail()));
    }
}
