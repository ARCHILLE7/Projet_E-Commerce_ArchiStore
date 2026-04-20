package com.gestion_produit.security_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurityDTO {

    private Long id;
    private String nom;
    private String email;
    private String adresse;
    private String password; // Include password for authentication
    private String role;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private Boolean isEmailVerified;
}
