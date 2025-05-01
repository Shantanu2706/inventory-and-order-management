package com.inventory.management.service;

import com.inventory.management.dto.InventoryDTO;
import com.inventory.management.dto.ProductPerformanceDTO;
import com.inventory.management.dto.SalesRequestDTO;
import com.inventory.management.dto.SalesResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ReportService {
    SalesResponseDTO getSalesReport(LocalDate startDate, LocalDate endDate);
    Page<InventoryDTO> getInventory(String name, String category, Integer minPrice, Integer maxPrice, Pageable pageable);
    Page<ProductPerformanceDTO> getProductPerformance(LocalDate startDate, LocalDate endDate,Pageable pageable);
}
