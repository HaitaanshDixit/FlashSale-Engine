package com.flashsale.flashsale_engine.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity  //  tells JPA this class is a database table. (without it JPA ignores it)
@Table(name = "sneakers") // naming the table, otherwise JPA uses class name only
@Getter // getter and setter saves boiler plate code, we don't write getX(), getY() many times
@Setter
@NoArgsConstructor // Lombok generates empty constructor new Sneaker() for empty obj
@AllArgsConstructor //  Lombok generates a constructor with all fields as parameters.
@Builder // Sneaker.builder().name("Air Jordan").price(...).build() ------- very clean,
public class Sneaker {

    @Id  // MARKS ID AS PRIMARY KEY OF THE TABLE
    @GeneratedValue(strategy = GenerationType.IDENTITY) //tells DB to auto-increment the id
    private Long id;

    @NotBlank(message = "Name cannot be blank")  // These ensure bad data never reaches the DB
    @Column(nullable = false) // Even if validation is bypassed somehow, the DB itself will reject null values.
    private String name;

    @NotBlank(message = "Brand cannot be blank")
    @Column(nullable = false)
    private String brand;

    @NotNull(message = "Price cannot be null") // These ensure bad data never reaches the DB
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") // // Even if validation is bypassed somehow, the DB itself states default value
    @Column(nullable = false)
    private BigDecimal price;  // never use double or float for money in Java (BigDecimal most precise)

    @NotNull(message = "Total stock cannot be null")
    @Min(value = 0, message = "Total stock cannot be negative")
    @Column(nullable = false)
    private Integer totalStock;

    @NotNull(message = "Flash sale stock cannot be null")
    @Min(value = 0, message = "Flash sale stock cannot be negative")
    @Column(nullable = false)
    private Integer flashSaleStock;

    @Column
    private String imageUrl;

    @Column
    private LocalDateTime saleStartTime;

    @Column
    private LocalDateTime saleEndTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist  // t sets createdAt to the current timestamp so you never have to set it manually.
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
