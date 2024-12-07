package com.trantienloi.laptopshop.controller.client;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.service.ProductService;
import org.springframework.web.bind.annotation.RequestParam;





@Controller
public class HomePageController {
    private final ProductService productService;
    public HomePageController(ProductService productService){
        this.productService = productService;
    }
    @GetMapping("/")
    public String getHomePage(Model model) {
        List<Product> lstProducts = this.productService.getAllProducts();
        model.addAttribute("lstProducts", lstProducts);
        return "client/homepage/show";
    }
    
    
}
