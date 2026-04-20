package com.gestion_produit.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hash")
@RequiredArgsConstructor
public class HashController {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/generate/{password}")
    public String generateHash(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }

    @GetMapping("/test/{password}/{hash}")
    public boolean testHash(@PathVariable String password, @PathVariable String hash) {
        return passwordEncoder.matches(password, hash);
    }
}
