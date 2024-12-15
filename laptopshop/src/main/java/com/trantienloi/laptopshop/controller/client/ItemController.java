package com.trantienloi.laptopshop.controller.client;


import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.trantienloi.laptopshop.domain.Cart;
import com.trantienloi.laptopshop.domain.CartDetail;
import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;





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
    @PostMapping("/delete-cart-product/{id}")
    public String postDeleteItem(@PathVariable long id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        long cart_detail_id = id;
        
        this.productService.handleDeleteCartProduct(cart_detail_id, session);        
        return "redirect:/cart";
    }
    @GetMapping("/cart")
    public String getCartPage(Model model, HttpServletRequest request) {
        User user = new User();
        HttpSession session = request.getSession(false); 
        long id = (long) session.getAttribute("id");
        user.setId(id);
        Cart cart = this.productService.fetchByUser(user);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetail();
        long total_price = 0;
        for (CartDetail cartDetail : cartDetails) {
            total_price += cartDetail.getPrice() * cartDetail.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", total_price);
        return "client/cart/show";
    }
    
    
}
