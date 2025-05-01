package com.inventory.management.dto;

import com.inventory.management.entity.Product;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailsDTO {
    private int orderId;
    private String status;
    private List<OrderDetailsProductDTO> products;
    private int totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String shippingAddress;

    public OrderDetailsDTO() {
    }

    public OrderDetailsDTO(int orderId, String status, List<OrderDetailsProductDTO> products, int totalPrice, LocalDateTime createdAt, LocalDateTime updatedAt, String shippingAddress) {
        this.orderId = orderId;
        this.status = status;
        this.products = products;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.shippingAddress = shippingAddress;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderDetailsProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderDetailsProductDTO> products) {
        this.products = products;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
