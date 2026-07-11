package com.flashsale.flashsale_engine.repository;

import com.flashsale.flashsale_engine.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Spring auto generates SQL from method name
    // SELECT * FROM orders WHERE user_id = ?
    List<Order> findByUserId(Long userId);

    // SELECT COUNT(*) > 0 FROM orders WHERE sneaker_id = ? AND user_id = ?
    boolean existsBySneakerIdAndUserId(Long sneakerId, Long userId);
}