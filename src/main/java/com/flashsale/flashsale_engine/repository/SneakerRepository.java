package com.flashsale.flashsale_engine.repository;

import com.flashsale.flashsale_engine.model.Sneaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SneakerRepository extends JpaRepository<Sneaker, Long> {

}