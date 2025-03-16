package com.trantienloi.laptopshop.controller.client;


import java.util.List;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.trantienloi.laptopshop.domain.Cart;
import com.trantienloi.laptopshop.domain.CartDetail;
import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;








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
        this.productService.handleAddProductToCart(product_id, email, session,1);        
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
        User currentUser = new User();// null
        HttpSession session = request.getSession(false);
        long id = (long) session.getAttribute("id");
        currentUser.setId(id);

        Cart cart = this.productService.fetchByUser(currentUser);
        List<CartDetail> cartDetails = (cart == null) ? new ArrayList<CartDetail>() : cart.getCartDetails();

        double totalPrice = 0;
        for (CartDetail cd : cartDetails) {
            totalPrice += cd.getPrice() * cd.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", totalPrice);

        model.addAttribute("cart", cart);

        return "client/cart/show";
    }
    @GetMapping("/checkout")
    public String getCheckPage(Model model,HttpServletRequest request) {
        User user = new User();
        HttpSession session = request.getSession(false);
        long id = (Long)session.getAttribute("id");
        user.setId(id);
        Cart cart = this.productService.fetchByUser(user);
        List<CartDetail> cartDetails = cart == null ? new ArrayList<CartDetail>() : cart.getCartDetails();
        double price = 0;
        for (CartDetail cartDetail : cartDetails) {
            price+= cartDetail.getPrice()*cartDetail.getQuantity();
        }
        model.addAttribute("cartDetails", cartDetails);
        model.addAttribute("totalPrice", price);
        return "client/cart/checkout";
    }
    @PostMapping("/confirm-checkout")
    public String getCheckOutPage(@ModelAttribute("cart") Cart cart) {
        List<CartDetail> cartDetails = (cart == null) ? new ArrayList<CartDetail>() : cart.getCartDetails();
        this.productService.handleUpdateCartBeforeCheckout(cartDetails);
        return "redirect:/checkout";
    }
    @PostMapping("/place-order")
    public String handlePlaceOrder(HttpServletRequest request,
                                    @RequestParam("receiverName") String receiverName,
                                    @RequestParam("receiverAddress") String receiverAddress,
                                    @RequestParam("receiverPhone") String receiverPhone) {
            HttpSession session = request.getSession(false);
            User user = new User();
            long id = (Long)session.getAttribute("id");
            user.setId(id);
            this.productService.handlePlaceOrder(user, session, receiverName, receiverAddress, receiverPhone);
        return "redirect:/thanks";
                                    }
    @GetMapping("/thanks")
    public String thanks(){
        return "client/cart/thanks";
    }
    @PostMapping("/add-product-from-view-detail")
    public String handleAddProductFromViewDetail(
            @RequestParam("id") long id,
            @RequestParam("quantity") long quantity,
            HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        String email = (String) session.getAttribute("email");
        // this.productService.handleAddProductToCart(email, id, session, quantity);
        this.productService.handleAddProductToCart(id, email, session, quantity);
        return "redirect:/product/" + id;
    }
}
    
