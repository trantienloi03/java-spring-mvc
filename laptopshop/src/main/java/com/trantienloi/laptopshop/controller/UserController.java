package com.trantienloi.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;


@Controller
public class UserController {
    private UserService userService;
        // DI: dependency injection
        public UserController(UserService userService) {
            this.userService = userService;
        }
    @RequestMapping("/")
    public String getHomePage(Model model) {
        String test = this.userService.handldeHello();
        List<User> lstUsers = userService.getAllUsers();
        System.out.println(lstUsers);
        List<User> lstUsersByEmail = userService.getUsersByEmail("Abc@gmail.com");
        System.out.println(lstUsersByEmail);
        model.addAttribute("TienLoi", test);
        model.addAttribute("name", "Tran tien Loi");
        return "hello";
    }
    
    
}
// @RestController
// public class UserController {
//     private UserService userService;
//     // DI: dependency injection
//     public UserController(UserService userService) {
//         this.userService = userService;
//     }

//     @GetMapping("")
//     public String getHomePage(){
//         return this.userService.handldeHello();
//     }
// }
