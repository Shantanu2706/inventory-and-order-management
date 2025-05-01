package com.inventory.management.service;


import com.inventory.management.dto.UserDTO;
import com.inventory.management.dto.LoginRequestDTO;
import com.inventory.management.dto.LoginResponseDTO;
import com.inventory.management.dto.RegisterRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
//import com.inventory.management.entity.User;

public interface AuthService {
    String saveUser(RegisterRequestDTO registerRequestDTO);
    LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO);
    ResponseEntity<UserDTO> getCurrentUser(Authentication authentication);
}
