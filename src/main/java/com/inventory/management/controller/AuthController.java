package com.inventory.management.controller;

import com.inventory.management.dto.UserDTO;
import com.inventory.management.dto.LoginRequestDTO;
import com.inventory.management.dto.LoginResponseDTO;
import com.inventory.management.dto.RegisterRequestDTO;
//import com.inventory.management.entity.User;
import com.inventory.management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public String saveUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO){
        return authService.saveUser(registerRequestDTO);
    }

    @PostMapping("/login")
    public LoginResponseDTO loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        return authService.loginUser(loginRequestDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication){
        return authService.getCurrentUser(authentication);
    }

}
