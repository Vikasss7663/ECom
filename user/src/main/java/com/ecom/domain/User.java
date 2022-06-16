package com.ecom.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;

enum UserRole {
    ADMIN,
    USER
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

    @Id
    private String userId;
    @NotBlank(message = "user.userName must be present")
    private String userName;
    @NotBlank(message = "user.userEmail must be present")
    private String userEmail;
    @NotBlank(message = "user.userPassword must be present")
    private String userPassword;
    @NotBlank(message = "user.userPhone must be present")
    private String userPhone;
    private UserRole userRole = UserRole.USER;
    private LocalDate createdAt = LocalDate.now();
    private LocalDate modifiedAt = LocalDate.now();
}