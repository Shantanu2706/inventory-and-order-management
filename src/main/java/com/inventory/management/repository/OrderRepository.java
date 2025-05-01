package com.inventory.management.repository;

import com.inventory.management.entity.Order;
import com.inventory.management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o from Order o WHERE " +
            "(:name IS NULL OR o.userName = :name) AND " +
            "(:status IS NULL OR o.status = :status)")
    Page<Order> findByUserNameAndStatus(@Param("name") String name, @Param("status") String status, Pageable pageable);

    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
