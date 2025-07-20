package com.vehicle.insurance_quote.controller;

import com.vehicle.insurance_quote.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username) {
        String token = JwtUtil.generateToken(username);
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }
}
