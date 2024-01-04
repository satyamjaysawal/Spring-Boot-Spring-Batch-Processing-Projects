package com.batchprocessing.BatchProcessingExampleDemo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // You can add custom query methods if needed
}
