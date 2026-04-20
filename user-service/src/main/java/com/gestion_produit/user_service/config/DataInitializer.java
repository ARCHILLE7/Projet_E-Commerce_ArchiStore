package com.gestion_produit.user_service.config;

import com.gestion_produit.user_service.dto.UserDTO;
import com.gestion_produit.user_service.entity.User;
import com.gestion_produit.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Initializing default users data...");
            
            // Admin user
            User admin = User.builder()
                    .nom("Administrateur")
                    .email("admin@example.com")
                    .adresse("123 Rue Admin, 10000 Rabat")
                    .phone("+212600000000")
                    .password(passwordEncoder.encode("Admin123!"))
                    .role(User.Role.ADMIN)
                    .isActive(true)
                    .isEmailVerified(true)
                    .build();
            
            // Regular users
            User user1 = User.builder()
                    .nom("Jean Dupont")
                    .email("jean@example.com")
                    .adresse("456 Avenue Hassan, 20000 Casablanca")
                    .phone("+212600000001")
                    .password(passwordEncoder.encode("User123!"))
                    .role(User.Role.USER)
                    .isActive(true)
                    .isEmailVerified(true)
                    .build();
            
            User user2 = User.builder()
                    .nom("Marie Martin")
                    .email("marie@example.com")
                    .adresse("789 Boulevard Mohammed, 30000 Marrakech")
                    .phone("+212600000002")
                    .password(passwordEncoder.encode("User123!"))
                    .role(User.Role.USER)
                    .isActive(true)
                    .isEmailVerified(true)
                    .build();
            
            User user3 = User.builder()
                    .nom("Ahmed Benali")
                    .email("ahmed@example.com")
                    .adresse("321 Rue Al Massa, 40000 Fès")
                    .phone("+212600000003")
                    .password(passwordEncoder.encode("User123!"))
                    .role(User.Role.USER)
                    .isActive(true)
                    .isEmailVerified(false)
                    .build();
            
            User user4 = User.builder()
                    .nom("Fatima Zahra")
                    .email("fatima@example.com")
                    .adresse("654 Place Yacoub, 50000 Agadir")
                    .phone("+212600000004")
                    .password(passwordEncoder.encode("User123!"))
                    .role(User.Role.USER)
                    .isActive(true)
                    .isEmailVerified(true)
                    .build();
            
            User user5 = User.builder()
                    .nom("Youssef Amrani")
                    .email("youssef@example.com")
                    .adresse("987 Route Tanger, 90000 Tanger")
                    .phone("+212600000005")
                    .password(passwordEncoder.encode("User123!"))
                    .role(User.Role.USER)
                    .isActive(false)
                    .isEmailVerified(true)
                    .build();

            userRepository.save(admin);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
            userRepository.save(user4);
            userRepository.save(user5);
            
            log.info("Default users initialized successfully!");
            log.info("Admin user: admin@example.com / Admin123!");
            log.info("Regular users: jean@example.com, marie@example.com, ahmed@example.com, fatima@example.com, youssef@example.com / User123!");
        }
    }
}
