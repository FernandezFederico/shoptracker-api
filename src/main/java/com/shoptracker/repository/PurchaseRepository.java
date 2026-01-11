package com.shoptracker.repository;

import com.shoptracker.model.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

        List<Purchase> findByPurchaseDateBetween(LocalDate dateFrom, LocalDate dateTo);
}
