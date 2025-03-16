package com.trantienloi.laptopshop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trantienloi.laptopshop.domain.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @SuppressWarnings("null")
    Product save(Product product);
    List<Product> findAll();
    Product findById(long id);
    void deleteById(long id);
    @SuppressWarnings("null")
    Page<Product> findAll(Pageable page);
    
}
