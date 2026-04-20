package com.gestion_produit.security_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    
    private Long id;
    private String nom;
    private String email;
    private String adresse;
    private String phone;
    private String role;
    private Boolean isActive;
    private Boolean isEmailVerified;
}
