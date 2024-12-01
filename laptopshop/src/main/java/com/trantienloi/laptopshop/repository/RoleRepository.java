package com.trantienloi.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trantienloi.laptopshop.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String roleName);
}
