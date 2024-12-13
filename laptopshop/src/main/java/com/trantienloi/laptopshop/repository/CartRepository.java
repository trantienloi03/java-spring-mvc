package com.trantienloi.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trantienloi.laptopshop.domain.Cart;
import com.trantienloi.laptopshop.domain.User;
@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    Cart findByUser(User user);
}
