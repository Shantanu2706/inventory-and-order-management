package com.inventory.management.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class UserDTO {
    @NotNull(message = "username is required")
    private String username;

    @NotNull(message = "roles are required")
    private List<String> roles;

    @NotNull(message = "enabled is required")
    private boolean enabled;

    public UserDTO(){}

    public UserDTO(String username, List<String> roles, boolean enabled) {
        this.username = username;
        this.roles = roles;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
