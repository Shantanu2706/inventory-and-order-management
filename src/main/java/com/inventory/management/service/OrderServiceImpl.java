package com.inventory.management.service;

import com.inventory.management.dto.*;
import com.inventory.management.entity.Order;
import com.inventory.management.entity.OrderItem;
import com.inventory.management.entity.Product;
import com.inventory.management.exception.ResourceConflictException;
import com.inventory.management.exception.ResourceNotFoundException;
import com.inventory.management.repository.OrderItemRepository;
import com.inventory.management.repository.OrderRepository;
import com.inventory.management.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public OrderResponseDTO saveOrder(OrderRequestDTO orderRequestDTO, Authentication authentication) {
        String username = authentication.getName();
        int totalPrice = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        Order order = new Order();
        order.setUserName(username);
        order.setStatus("Pending");
        order.setShippingAddress(orderRequestDTO.getShippingAddress());

        for(OrderRequestProductDTO dto:orderRequestDTO.getProducts()){
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));

            if (product.getStock() < dto.getQuantity()) {
                throw new ResourceConflictException("Insufficient stock for product id: " + dto.getProductId());
            }

            // reduce stock after order
            product.setStock(product.getStock() - dto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            totalPrice += dto.getQuantity() * product.getPrice();
            orderItems.add(orderItem);
        }

        // save order
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        // save OrderItems
        for (OrderItem orderItem : orderItems) {
            orderItemRepository.save(orderItem);
        }

        // create response DTO
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(savedOrder.getId());
        orderResponseDTO.setStatus(savedOrder.getStatus());
        orderResponseDTO.setTotalPrice(savedOrder.getTotalPrice());
        orderResponseDTO.setShippingAddress(savedOrder.getShippingAddress());

        return orderResponseDTO;

    }

    @Override
    public Page<AllOrdersResponseDTO> getOrders(String status, Pageable pageable, Authentication authentication) {
        String username;
        // check if user is admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if(isAdmin){
            username = null;
        }else{
            username = authentication.getName();
        }

        Page<Order> orderList = orderRepository.findByUserNameAndStatus(username, status, pageable);

        return orderList.map(order -> {
            AllOrdersResponseDTO orderResponseDTO = new AllOrdersResponseDTO();
            orderResponseDTO.setOrderId(order.getId());
            orderResponseDTO.setStatus(order.getStatus());
            orderResponseDTO.setTotalPrice(order.getTotalPrice());
            orderResponseDTO.setOrderDate(order.getCreatedAt());
            return orderResponseDTO;
        });
    }

    @Override
    public OrderDetailsDTO getOrderById(int orderId, Authentication authentication) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // check if order belongs to current user
        String username = authentication.getName();
        if (!order.getUserName().equals(username)){
            // check if user is admin
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            if (!isAdmin) {
                throw new AccessDeniedException("You do not have permission to view this order");
            }
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        return getOrderDetailsDTO(orderItems, order);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(int orderId, UpdateOrderDTO updateOrderDTO, Authentication authentication) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        String username = authentication.getName();

        if(!isAdmin && !order.getUserName().equals(username)){
            throw new AccessDeniedException("You do not have permission to update this order");
        }

        if(updateOrderDTO.getStatus().equals("Cancelled") && !order.getStatus().equals("Pending")){
            throw new ResourceConflictException("You can only cancel a pending order");
        }

        if(updateOrderDTO.getStatus().equals("Cancelled")){
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            for(OrderItem item:orderItems){
                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + item.getProduct().getId()));
                product.setStock(product.getStock() + item.getQuantity());
                productRepository.save(product);
            }
        }else{
            if(!isAdmin){
                throw new AccessDeniedException("You do not have permission to update this order");
            }
        }
        order.setStatus(updateOrderDTO.getStatus());
        Order updatedOrder = orderRepository.save(order);

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(updatedOrder.getId());
        orderResponseDTO.setStatus(updatedOrder.getStatus());
        orderResponseDTO.setTotalPrice(updatedOrder.getTotalPrice());
        orderResponseDTO.setShippingAddress(updatedOrder.getShippingAddress());

        return orderResponseDTO;
    }

    private static OrderDetailsDTO getOrderDetailsDTO(List<OrderItem> orderItems, Order order) {
        List<OrderDetailsProductDTO> orderDetailsProductDTOList = getOrderDetailsProductDTOS(orderItems);

        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.setOrderId(order.getId());
        orderDetailsDTO.setStatus(order.getStatus());
        orderDetailsDTO.setTotalPrice(order.getTotalPrice());
        orderDetailsDTO.setShippingAddress(order.getShippingAddress());
        orderDetailsDTO.setCreatedAt(order.getCreatedAt());
        orderDetailsDTO.setUpdatedAt(order.getUpdatedAt());
        orderDetailsDTO.setProducts(orderDetailsProductDTOList);
        return orderDetailsDTO;
    }

    private static List<OrderDetailsProductDTO> getOrderDetailsProductDTOS(List<OrderItem> orderItems) {
        List<OrderDetailsProductDTO> orderDetailsProductDTOList = new ArrayList<>();
        for (OrderItem item : orderItems) {
            OrderDetailsProductDTO orderDetailsProductDTO = new OrderDetailsProductDTO();
            orderDetailsProductDTO.setId(item.getProduct().getId());
            orderDetailsProductDTO.setName(item.getProduct().getName());
            orderDetailsProductDTO.setQuantity(item.getQuantity());
            orderDetailsProductDTO.setPrice(item.getUnitPrice());
            orderDetailsProductDTOList.add(orderDetailsProductDTO);
        }
        return orderDetailsProductDTOList;
    }
}
