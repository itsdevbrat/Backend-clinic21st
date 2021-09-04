package com.clinic.website.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AppUser {
    @Id
    private String id;
    private String password;
    private String name;
    private String designation;
    private String phoneNumber;
    private String email;
    private String instituteName;
    private String address;
    private LocalDateTime dob;
    private LocalDateTime expireDate;
    private List<Vertical> verticals;
    private List<String> roles;
    private boolean enabled;

    public void setDob(LocalDate dob) {
        this.dob = dob.atStartOfDay();
    }

}
