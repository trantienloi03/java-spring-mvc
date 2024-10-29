package com.trantienloi.laptopshop.service;

import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.repository.UserRepository;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public List<User> getUsersByEmail(String email){
        List<User> lst = userRepository.findByEmail(email);
        return lst;
    }
}
