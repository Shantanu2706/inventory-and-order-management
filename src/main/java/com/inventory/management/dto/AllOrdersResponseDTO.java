package com.inventory.management.dto;

import java.time.LocalDateTime;

public class AllOrdersResponseDTO {
    private int orderId;
    private String status;
    private int totalPrice;
    private LocalDateTime orderDate;

    public AllOrdersResponseDTO() {}

    public AllOrdersResponseDTO(int orderId, String status, int totalPrice, LocalDateTime orderDate) {
        this.orderId = orderId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
