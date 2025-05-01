package com.inventory.management.controller;

import com.inventory.management.dto.*;
import com.inventory.management.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponseDTO createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO, Authentication authentication) {
        return orderService.saveOrder(orderRequestDTO, authentication);
    }

    @GetMapping
    public Page<AllOrdersResponseDTO> getOrders(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Authentication authentication) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderService.getOrders(status, pageable, authentication);
    }

    @GetMapping("/{id}")
    public OrderDetailsDTO getOrderById(@PathVariable int id, Authentication authentication) {
        return orderService.getOrderById(id, authentication);
    }

    @PutMapping("/{id}/status")
    public OrderResponseDTO updateOrderStatus(@PathVariable int id, @Valid @RequestBody UpdateOrderDTO updateOrderDTO, Authentication authentication) {
        return orderService.updateOrderStatus(id, updateOrderDTO, authentication);
    }


}
