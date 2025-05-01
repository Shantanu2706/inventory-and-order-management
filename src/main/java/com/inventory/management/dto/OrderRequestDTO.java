package com.inventory.management.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequestDTO {
    @NotNull(message = "products are required")
    private List<OrderRequestProductDTO> products;

    @NotNull(message = "shippingAddress is required")
    private String shippingAddress;

    public OrderRequestDTO(){}

    public OrderRequestDTO(List<OrderRequestProductDTO> products, String shippingAddress) {
        this.products = products;
        this.shippingAddress = shippingAddress;
    }

    public List<OrderRequestProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderRequestProductDTO> products) {
        this.products = products;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
