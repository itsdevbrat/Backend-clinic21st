package com.clinic.website.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

//import javax.validation.constraints.Pattern;
import java.time.Instant;
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
    @TextIndexed(weight=3) private String userName;
    private String designation;
    @TextIndexed
//    @Pattern(regexp = "[0-9]{10,11}")
    private String phoneNumber;
    @TextIndexed(weight=2) private String email;
    private String instituteName;
    private String address;
    private LocalDateTime birthDate;
    private LocalDateTime expireDate;
    private Instant createdDate;
    private List<String> verticals;
    private List<Vertical> userVerticals;
    private boolean enabled;

    public void setDob(LocalDate birthDate) {
        this.birthDate = birthDate.atStartOfDay();
    }

}
