package com.trantienloi.laptopshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trantienloi.laptopshop.domain.Order;
import com.trantienloi.laptopshop.domain.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order order);
    List<Order> findAll();
    Optional<Order> findById(long id);
    List<Order> findByUser(User user);
    Page<Order> findAll(Pageable pageable);
}
