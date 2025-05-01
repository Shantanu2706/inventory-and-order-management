package com.inventory.management.service;

import com.inventory.management.dto.InventoryDTO;
import com.inventory.management.entity.Product;
import com.inventory.management.exception.ResourceConflictException;
import com.inventory.management.exception.ResourceNotFoundException;
import com.inventory.management.repository.ProductRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> getFilteredProducts(String name, String category,Integer minPrice, Integer maxPrice,Pageable pageable) {
        return productRepository.findByNameAndCategory(name,category,minPrice,maxPrice,pageable);
    }



    @Override
    public Product getProductById(int id) {
        Optional<Product> result = productRepository.findById(id);
        Product product = null;
        if(result.isPresent()){
            product = result.get();
        }else{
            throw new ResourceNotFoundException("Product not found with id - " + id);
        }
        return product;
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        // if product with same name exists and we are not updating product
         if(product.getId() == 0) {
             if (productRepository.findByName(product.getName()) != null) {
                 throw new ResourceConflictException("Product with same name already exists");
             }
         }

        return productRepository.save(product);

    }

    @Override
    @Transactional
    public String deleteProductById(int id) {
        productRepository.deleteById(id);
        return "Product with id " + id + " deleted successfully";
    }
}
