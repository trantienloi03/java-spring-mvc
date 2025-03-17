package com.trantienloi.laptopshop.service;

import org.eclipse.tags.shaded.org.apache.regexp.recompile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.Role;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.domain.dto.registerUser;
import com.trantienloi.laptopshop.repository.OrderRepository;
import com.trantienloi.laptopshop.repository.ProductRepository;
import com.trantienloi.laptopshop.repository.RoleRepository;
import com.trantienloi.laptopshop.repository.UserRepository;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, 
                        RoleRepository roleRepository,
                        OrderRepository orderRepository,
                        ProductRepository productRepository ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }
    public String handldeHello(){
        return "helllo from controller";
    }
    public User handleSaveUser(User trantienloi){
        User TienLoi = userRepository.save(trantienloi);
        return TienLoi;
    }
    public User getUserById(long id){
        User TienLoi = userRepository.findById(id);
        return TienLoi;
    }
    public Page<User> getAllUsers(Pageable pageable){
        Page<User> lstUsers = userRepository.findAll(pageable);
        return lstUsers;
    }
    public void DeleteUserByID(long id){
        this.userRepository.deleteById(id);
    }
    public List<User> getUsersByEmail(String email){
        List<User> lst = userRepository.findOneByEmail(email);
        return lst;
    }
    public Role getRoleByName (String name){
        return this.roleRepository.findByName(name);
    }
    public User regesterDTOtoUser( registerUser registerUser){
        User user = new User();
        user.setFullname(registerUser.getFirstName() + " " + registerUser.getLastName());
        user.setEmail(registerUser.getEmail());
        user.setPassword(registerUser.getPassword());

        return user;

    }
    public boolean checkExistEmail(String email){
        return this.userRepository.existsByEmail(email);
    }
    public User getUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
    public long countUser(){
        return this.userRepository.count();
    }
    public long countProducts(){
        return this.productRepository.count();
    }
    public long countOrders(){
        return this.orderRepository.count();
    }
}
