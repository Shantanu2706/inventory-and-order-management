package com.inventory.management.dto;

import java.util.Date;

public class LoginResponseDTO {
    private String token;
    private Date issuedAt;
    private Date expiresAt;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, Date issuedAt, Date expiresAt) {
        this.token = token;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }
}
