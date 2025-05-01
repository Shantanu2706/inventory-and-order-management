package com.inventory.management.service;

import com.inventory.management.dto.InventoryDTO;
import com.inventory.management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<Product> getAllProducts(Pageable pageable);
    Page<Product> getFilteredProducts(String name, String category, Integer minPrice, Integer maxPrice, Pageable pageable);

    Product getProductById(int id);
    Product saveProduct(Product product);
    String deleteProductById(int id);
}
