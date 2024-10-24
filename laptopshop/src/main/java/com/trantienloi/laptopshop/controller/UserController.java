package com.trantienloi.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trantienloi.laptopshop.service.UserService;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
public class UserController {
    private UserService userService;
        // DI: dependency injection
        public UserController(UserService userService) {
            this.userService = userService;
        }
    @RequestMapping("/")
    public String getHomePage() {
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
