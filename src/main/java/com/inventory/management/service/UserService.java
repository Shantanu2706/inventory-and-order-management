package com.inventory.management.service;

import com.inventory.management.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDTO> getAllUsers(Pageable pageable);
    UserDTO getUserByName(String username);
    UserDTO updateUser(UserDTO userDTO);
}
