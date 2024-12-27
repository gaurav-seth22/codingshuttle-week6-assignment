package com.codingshuttle.SecurityApp.SecurityApplication.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {
    @GetMapping("/debug/auth")
    public ResponseEntity<?> debugAuth(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.ok("No authentication found");
        }
        Map<String, Object> details = new HashMap<>();
        details.put("name", authentication.getName());
        details.put("authorities", authentication.getAuthorities());
        details.put("principal", authentication.getPrincipal());
        details.put("isAuthenticated", authentication.isAuthenticated());
        return ResponseEntity.ok(details);
    }
}
