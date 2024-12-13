package com.trantienloi.laptopshop.controller.client;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class ItemController {
    private final ProductService productService;
    public ItemController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/product/{id}")
    public String getMethodName(Model model ,@PathVariable long id) {
         Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "client/product/detail";
    }
    @PostMapping("/add-product-to-cart/{id}")
    public String postMethodName(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long product_id = id;
        String email = (String) session.getAttribute("email");
        this.productService.handleAddProductToCart(product_id, email, session);        
        return "redirect:/";
    }
    
    
    
}
