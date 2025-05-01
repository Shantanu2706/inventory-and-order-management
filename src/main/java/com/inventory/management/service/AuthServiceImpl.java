package com.inventory.management.service;

import com.inventory.management.dto.UserDTO;
import com.inventory.management.dto.LoginRequestDTO;
import com.inventory.management.dto.LoginResponseDTO;
import com.inventory.management.dto.RegisterRequestDTO;
//import com.inventory.management.entity.Authority;
//import com.inventory.management.entity.User;
//import com.inventory.management.entity.User;
//import com.inventory.management.repository.AuthorityRepository;
//import com.inventory.management.repository.UserRepository;
import com.inventory.management.exception.ResourceConflictException;
import com.inventory.management.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final JdbcUserDetailsManager userDetailsManager;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(BCryptPasswordEncoder passwordEncoder, JdbcUserDetailsManager userDetailsManager, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsManager = userDetailsManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public String saveUser(RegisterRequestDTO registerRequestDTO) {
        if(userDetailsManager.userExists(registerRequestDTO.getUsername())){
            throw new ResourceConflictException("User with username already exists");
        }
        UserDetails user = User.builder()
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .roles(registerRequestDTO.getRoles().toArray(new String[0]))
                .build();

        userDetailsManager.createUser(user);

        return "User registered successfully";
    }

    @Override
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );
        LoginResponseDTO loginResponseDTO;

        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(authentication.isAuthenticated()){
            loginResponseDTO = jwtUtil.generateToken(loginRequestDTO.getUsername());
            return loginResponseDTO;
        }

        throw new RuntimeException("Authentication failed.");
    }

    @Override
    public ResponseEntity<UserDTO> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetails userDetails = userDetailsManager.loadUserByUsername(authentication.getName());

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userDetails.getUsername());
        userDTO.setEnabled(userDetails.isEnabled());
        userDTO.setRoles(userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList());

        return ResponseEntity.ok(userDTO);
    }
}
