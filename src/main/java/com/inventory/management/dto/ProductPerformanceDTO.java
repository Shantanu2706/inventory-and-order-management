package com.inventory.management.dto;

public class ProductPerformanceDTO {
    private int productId;
    private String productName;
    private int totalSales;

    public ProductPerformanceDTO() {
    }

    public ProductPerformanceDTO(int productId, String productName, int totalSales) {
        this.productId = productId;
        this.productName = productName;
        this.totalSales = totalSales;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }
}
