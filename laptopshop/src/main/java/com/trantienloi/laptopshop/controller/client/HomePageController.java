package com.trantienloi.laptopshop.controller.client;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.domain.dto.registerUser;
import com.trantienloi.laptopshop.service.ProductService;
import com.trantienloi.laptopshop.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;






@Controller
public class HomePageController {
    private final ProductService productService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public HomePageController(ProductService productService, UserService userService, PasswordEncoder passwordEncoder){
        this.productService = productService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> lstProducts = this.productService.getAllProducts();
        model.addAttribute("lstProducts", lstProducts);
        return "client/homepage/show";
    }
    @GetMapping("/register")
    public String getResgisterPage(Model model){
        registerUser registerUser = new registerUser();
        model.addAttribute("registerUser", registerUser);
        return "client/auth/register";
    }
    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute("registerUser") registerUser registerUser,
                                BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "client/auth/register";
        }
        User user = this.userService.regesterDTOtoUser(registerUser);
        String hashPass = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPass);
        user.setRole(this.userService.getRoleByName("USER"));
        
        this.userService.handleSaveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){

        return "client/auth/login";
    }
    @GetMapping("/access-deny")
    public String getAccessDenyPage(Model model){

        return "client/auth/deny";
    }
    
    
    
}
