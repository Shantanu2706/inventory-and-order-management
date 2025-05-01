package com.inventory.management.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateOrderDTO {
    @NotNull(message = "status is required")
    private String status;

    public UpdateOrderDTO() {
    }

    public UpdateOrderDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
