package com.trantienloi.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.trantienloi.laptopshop.domain.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    User save(User trantienloi);
    User findById(long id);
    void deleteById(long id);
    List<User> findOneByEmail(String email);
    boolean existsByEmail(String email);
    User findByEmail(String email);


    
} 
