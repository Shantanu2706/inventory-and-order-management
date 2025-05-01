package com.inventory.management.dto;

import jakarta.validation.constraints.NotNull;

public class RestockRequestDTO {
    @NotNull(message = "quantity is required")
    private int quantity;

    public RestockRequestDTO() {
    }

    public RestockRequestDTO(int productId, int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
