package com.inventory.management.controller;

import com.inventory.management.dto.UserDTO;
import com.inventory.management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public Page<UserDTO> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{username}")
    public UserDTO getUserByName(
            @PathVariable String username
    ){
        return userService.getUserByName(username);
    }

    @PutMapping
    public UserDTO updateUser(
            @Valid @RequestBody UserDTO userDTO
    ){
        return userService.updateUser(userDTO);
    }


}
