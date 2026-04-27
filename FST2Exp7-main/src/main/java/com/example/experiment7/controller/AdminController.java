package com.example.experiment7.controller;

import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public Map<String, String> dashboard(Principal principal) {
        return Map.of(
                "message", "Welcome, admin",
                "username", principal.getName());
    }
}
