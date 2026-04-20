package com.gestion_produit.user_service.dto;

import com.gestion_produit.user_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .id(user.getId())
                .nom(user.getNom())
                .email(user.getEmail())
                .adresse(user.getAdresse())
                .role(user.getRole())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .lastLogin(user.getLastLogin())
                .isActive(user.getIsActive())
                .isEmailVerified(user.getIsEmailVerified())
                .build();
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        return User.builder()
                .id(userDTO.getId())
                .nom(userDTO.getNom())
                .email(userDTO.getEmail())
                .adresse(userDTO.getAdresse())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .phone(userDTO.getPhone())
                .isActive(userDTO.getIsActive())
                .isEmailVerified(userDTO.getIsEmailVerified())
                .build();
    }

    public void updateUserFromDTO(UserDTO userDTO, User user) {
        if (userDTO == null || user == null) {
            return;
        }

        user.setNom(userDTO.getNom());
        user.setEmail(userDTO.getEmail());
        user.setAdresse(userDTO.getAdresse());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(userDTO.getPassword());
        }
        user.setRole(userDTO.getRole());
        user.setPhone(userDTO.getPhone());
        user.setIsActive(userDTO.getIsActive());
        user.setIsEmailVerified(userDTO.getIsEmailVerified());
    }
}
