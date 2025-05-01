package com.inventory.management.dto;

import java.util.List;
import java.util.Map;

public class SalesResponseDTO {
    private int totalSales;
    private Map<String,Integer> categoryWiseSales;

    public SalesResponseDTO() {
    }

    public SalesResponseDTO(int totalSales, Map<String,Integer> categoryWiseSales) {
        this.totalSales = totalSales;
        this.categoryWiseSales = categoryWiseSales;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public Map<String, Integer> getCategoryWiseSales() {
        return categoryWiseSales;
    }

    public void setCategoryWiseSales(Map<String, Integer> categoryWiseSales) {
        this.categoryWiseSales = categoryWiseSales;
    }
}
