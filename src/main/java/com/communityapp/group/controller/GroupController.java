package com.communityapp.group.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    @PostMapping("/create")
    public ResponseEntity<?> createGroup(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("error", "Debes iniciar sesión para crear un grupo"));
        }
        return ResponseEntity.ok(Map.of("message", "Has iniciado sesion correctamente"));
    }
}
