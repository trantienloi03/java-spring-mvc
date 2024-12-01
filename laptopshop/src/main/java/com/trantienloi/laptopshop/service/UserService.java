package com.trantienloi.laptopshop.service;

import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.Role;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.repository.RoleRepository;
import com.trantienloi.laptopshop.repository.UserRepository;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
    public List<User> getAllUsers(){
        List<User> lstUsers = userRepository.findAll();
        return lstUsers;
    }
    public void DeleteUserByID(long id){
        this.userRepository.deleteById(id);
    }
    public List<User> getUsersByEmail(String email){
        List<User> lst = userRepository.findByEmail(email);
        return lst;
    }
    public Role getRoleByName (String name){
        return this.roleRepository.findByName(name);
    }
}
