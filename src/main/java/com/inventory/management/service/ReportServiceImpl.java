package com.inventory.management.service;

import com.inventory.management.dto.InventoryDTO;
import com.inventory.management.dto.ProductPerformanceDTO;
import com.inventory.management.dto.SalesRequestDTO;
import com.inventory.management.dto.SalesResponseDTO;
import com.inventory.management.entity.Order;
import com.inventory.management.entity.OrderItem;
import com.inventory.management.entity.Product;
import com.inventory.management.repository.OrderItemRepository;
import com.inventory.management.repository.OrderRepository;
import com.inventory.management.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService{
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public ReportServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public SalesResponseDTO getSalesReport(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Order> orderList = orderRepository.findByCreatedAtBetween(startDateTime, endDateTime);

        // Remove Pending and Cancelled orders
        orderList.removeIf(order -> order.getStatus().equals("Pending") || order.getStatus().equals("Cancelled"));

        int totalSales = 0;
        Map<String, Integer> categoryWiseSales = new HashMap<>();

        // get orderitems with order Ids
        for(Order order: orderList){
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            for(OrderItem orderItem: orderItems){
                Product product = orderItem.getProduct();
                int totalPrice = orderItem.getQuantity() * product.getPrice();
                String category = product.getCategory();
                categoryWiseSales.put(category, categoryWiseSales.getOrDefault(category, 0) + totalPrice);
                totalSales += totalPrice;
            }
        }
        SalesResponseDTO salesResponseDTO = new SalesResponseDTO();
        salesResponseDTO.setTotalSales(totalSales);
        salesResponseDTO.setCategoryWiseSales(categoryWiseSales);

        return salesResponseDTO;
    }

    @Override
    public Page<InventoryDTO> getInventory(String name, String category, Integer minPrice, Integer maxPrice, Pageable pageable) {
        Page<Product> productList = productRepository.findByNameAndCategory(name,category,minPrice,maxPrice,pageable);

        Page<InventoryDTO> inventoryDTOPage = productList.map(product -> {
            InventoryDTO inventoryDTO = new InventoryDTO();
            inventoryDTO.setId(product.getId());
            inventoryDTO.setName(product.getName());
            inventoryDTO.setStock(product.getStock());
            return inventoryDTO;
        });
        return inventoryDTOPage;
    }

    @Override
    public Page<ProductPerformanceDTO> getProductPerformance(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Order> orderList = orderRepository.findByCreatedAtBetween(startDateTime, endDateTime);

        // Remove Pending and Cancelled orders
        orderList.removeIf(order -> order.getStatus().equals("Pending") || order.getStatus().equals("Cancelled"));

        Map<Integer,Integer> productSalesMap = new HashMap<>();
        for(Order order: orderList){
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            for(OrderItem orderItem: orderItems){
                Product product = orderItem.getProduct();
                int totalPrice = orderItem.getQuantity() * product.getPrice();
                productSalesMap.put(product.getId(), productSalesMap.getOrDefault(product.getId(), 0) + totalPrice);
            }
        }

        List<ProductPerformanceDTO> dtoList = productSalesMap.entrySet().stream()
                .map(entry -> {
                    Product product = productRepository.findById(entry.getKey()).orElse(null);
                    if (product != null) {
                        ProductPerformanceDTO productPerformanceDTO = new ProductPerformanceDTO();
                        productPerformanceDTO.setProductId(product.getId());
                        productPerformanceDTO.setProductName(product.getName());
                        productPerformanceDTO.setTotalSales(entry.getValue());
                        return productPerformanceDTO;
                    }
                    return null;
                })
                .filter(dto -> dto != null)
                .toList();

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), dtoList.size());

        List<ProductPerformanceDTO> pagedList = dtoList.subList(start, end);

        return new PageImpl<>(pagedList, pageable, dtoList.size());

    }
}
