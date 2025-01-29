package com.Ali.EcommerceApp.repo;

import com.Ali.EcommerceApp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
}
