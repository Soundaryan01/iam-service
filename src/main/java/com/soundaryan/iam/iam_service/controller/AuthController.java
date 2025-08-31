package com.soundaryan.iam.iam_service.controller;

import com.soundaryan.iam.iam_service.dto.RegisterRequest;
import com.soundaryan.iam.iam_service.dto.UserResponse;
import com.soundaryan.iam.iam_service.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse userResponse = authService.registerUser(request);
        return ResponseEntity.ok(userResponse);
    }

}

