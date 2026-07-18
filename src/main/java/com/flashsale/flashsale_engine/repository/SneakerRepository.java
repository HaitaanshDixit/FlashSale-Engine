package com.flashsale.flashsale_engine.repository;

import com.flashsale.flashsale_engine.model.Sneaker;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SneakerRepository extends JpaRepository<Sneaker, Long> {

    // Pessimistic lock — locks the row until transaction completes
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Sneaker s WHERE s.id = :id")
    Optional<Sneaker> findByIdWithPessimisticLock(@Param("id") Long id);
}