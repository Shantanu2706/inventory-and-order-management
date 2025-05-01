package com.inventory.management.service;

import com.inventory.management.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderResponseDTO saveOrder(OrderRequestDTO orderRequestDTO, Authentication authentication);
    Page<AllOrdersResponseDTO> getOrders(String status, Pageable pageable, Authentication authentication);
    OrderDetailsDTO getOrderById(int orderId, Authentication authentication);
    OrderResponseDTO updateOrderStatus(int orderId, UpdateOrderDTO updateOrderDTO, Authentication authentication);
}
