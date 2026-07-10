package com.flashsale.flashsale_engine.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which sneaker was ordered
    @ManyToOne(fetch = FetchType.LAZY)  //  defines the relationship between Order and Sneaker. Many orders can belong to one sneaker. Lazy fetch only loads the sneaker when you explicitly access it. This is a performance optimization — why load sneaker data if you only needed the order?
    @JoinColumn(name = "sneaker_id", nullable = false) // this creates a sneaker_id column that stores the foreign key reference to the sneakers table.
    private Sneaker sneaker;

    // Which user placed the order (simple userId for now, no auth yet)
    @Column(nullable = false)
    private Long userId;

    // Quantity always 1 in flash sale but kept flexible
    @Column(nullable = false)
    private Integer quantity;

    // Price locked at time of purchase
    @Column(nullable = false)
    private BigDecimal priceAtPurchase;

    // Order status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}