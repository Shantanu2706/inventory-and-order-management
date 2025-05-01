package com.inventory.management.service;

import com.inventory.management.dto.UserDTO;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcUserDetailsManager jdbcUserDetailsManager;

    public UserServiceImpl(JdbcTemplate jdbcTemplate, JdbcUserDetailsManager jdbcUserDetailsManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcUserDetailsManager = jdbcUserDetailsManager;
    }
    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        // Fetch all users from the database using JdbcUserDetailsManager
        String sql = "SELECT u.username, u.enabled, a.authority " +
                "FROM users u LEFT JOIN authorities a ON u.username = a.username";

        List<UserDTO> userDTOList;
        Map<String, UserDTO> userRolesMap = new HashMap<>();

        jdbcTemplate.query(sql, rs -> {
            String username = rs.getString("username");
            String authority = rs.getString("authority");
            boolean enabled = rs.getBoolean("enabled");

            userRolesMap.computeIfAbsent(username, k -> new UserDTO(username, new ArrayList<>(), enabled)).getRoles().add(authority);
        });

        userDTOList = new ArrayList<>(userRolesMap.values());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), userDTOList.size());

        List<UserDTO> pagedList = userDTOList.subList(start, end);

        return new PageImpl<>(pagedList, pageable, userDTOList.size());
    }

    @Override
    public UserDTO getUserByName(String username) {
        UserDetails userDetails = jdbcUserDetailsManager.loadUserByUsername(username);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(userDetails.getUsername());
        userDTO.setEnabled(userDetails.isEnabled());
        userDTO.setRoles(userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList());

        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        String username = userDTO.getUsername();

        // 1. Update enabled status
        jdbcTemplate.update("UPDATE users SET enabled = ? WHERE username = ?",
                userDTO.isEnabled(), username);

        // 2. Remove all current roles
        jdbcTemplate.update("DELETE FROM authorities WHERE username = ?", username);

        // 3. Add new roles
        for (String role : userDTO.getRoles()) {
            jdbcTemplate.update("INSERT INTO authorities (username, authority) VALUES (?, ?)",
                    username, role);
        }

        return userDTO;
    }
}
