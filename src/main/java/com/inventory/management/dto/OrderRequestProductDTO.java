package com.inventory.management.dto;

import jakarta.validation.constraints.NotNull;

public class OrderRequestProductDTO {
    @NotNull(message = "productId is required")
    private int productId;

    @NotNull(message = "quantity is required")
    private int quantity;

    public OrderRequestProductDTO() {
    }

    public OrderRequestProductDTO(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
