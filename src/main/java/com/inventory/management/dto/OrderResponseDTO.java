package com.inventory.management.dto;

public class OrderResponseDTO {
    private int orderId;
    private String status;
    private int totalPrice;
    private String shippingAddress;

    public OrderResponseDTO() {}

    public OrderResponseDTO(int orderId, String status, int totalPrice, String shippingAddress) {
        this.orderId = orderId;
        this.status = status;
        this.totalPrice = totalPrice;
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

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
