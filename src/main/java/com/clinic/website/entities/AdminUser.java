package com.clinic.website.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AdminUser {
    @Id
    private String id;
    private String password;
    private String userName;
    private String email;
    private Instant createdDate;
    private boolean enabled;
}
