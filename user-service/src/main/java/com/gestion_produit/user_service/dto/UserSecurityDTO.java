package com.gestion_produit.user_service.dto;

import com.gestion_produit.user_service.entity.User;
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
    private User.Role role;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private Boolean isEmailVerified;
}
