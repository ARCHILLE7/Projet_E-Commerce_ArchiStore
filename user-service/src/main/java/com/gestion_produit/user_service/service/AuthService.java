package com.gestion_produit.user_service.service;

import com.gestion_produit.user_service.dto.LoginRequest;
import com.gestion_produit.user_service.dto.LoginResponse;
import com.gestion_produit.user_service.entity.User;
import com.gestion_produit.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Attempting login for email: {}", loginRequest.getEmail());
        
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + loginRequest.getEmail()));
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        if (!user.getIsActive()) {
            throw new RuntimeException("User account is not active");
        }
        
        String token = jwtTokenProvider.generateToken(user.getEmail());
        
        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .isActive(user.getIsActive())
                .isEmailVerified(user.getIsEmailVerified())
                .build();
    }

    public void register(LoginRequest registerRequest) {
        log.info("Attempting registration for email: {}", registerRequest.getEmail());
        
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = User.builder()
                .nom("New User")
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .adresse("Default Address")
                .isActive(true)
                .isEmailVerified(false)
                .role(User.Role.USER)
                .build();
        
        userRepository.save(user);
        log.info("User registered successfully: {}", registerRequest.getEmail());
    }

    public boolean validateToken(String token) {
        try {
            return jwtTokenProvider.validateToken(token);
        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }
}
