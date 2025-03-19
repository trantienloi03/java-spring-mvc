package com.trantienloi.laptopshop.controller.client;


import java.io.UnsupportedEncodingException;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.trantienloi.laptopshop.domain.Cart;
import com.trantienloi.laptopshop.domain.CartDetail;
import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.domain.Product_;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.domain.dto.ProductCriteriaDTO;
import com.trantienloi.laptopshop.service.ProductService;
import com.trantienloi.laptopshop.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;








@Controller
public class ItemController {
    private final ProductService productService;
    private final VNPayService vNPayService;
    public ItemController(ProductService productService, VNPayService vNPayService){
        this.productService = productService;
        this.vNPayService = vNPayService;
    }
    @GetMapping("/product/{id}")
    public String getMethodName(Model model ,@PathVariable long id) {
         Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "client/product/detail";
    }
    @GetMapping("/products")
    public String getPoducts(Model model ,ProductCriteriaDTO productCriteriaDTO, HttpServletRequest request) {
        int page = 1;
        try {
            if(productCriteriaDTO.getPage().isPresent()){
                page = Integer.parseInt(productCriteriaDTO.getPage().get());
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page-1, 3);
        if(productCriteriaDTO.getSort() != null && productCriteriaDTO.getSort().isPresent()){
            String sort = productCriteriaDTO.getSort().get();
            if(sort.equals("gia-tang-dan")){
                 pageable = PageRequest.of(page-1, 3, Sort.by(Product_.PRICE).ascending());
            }
            else if(sort.equals("gia-giam-dan")){
                 pageable = PageRequest.of(page-1, 3, Sort.by(Product_.PRICE).descending());
            }else{
                pageable = PageRequest.of(page-1, 3);
            }
        }
        
        Page<Product> lstProducts = this.productService.getAllProductsWithSpec(productCriteriaDTO ,pageable);
        //case0 name like
        // String name = nameOptional.isPresent() ? nameOptional.get() : "";
        // Page<Product> lstProducts = this.productService.getAllProductsWithSpec(pageable, name);

        //case1 min price
        // Double min = minOptional.isPresent() ? Double.parseDouble(minOptional.get()) : 0;
        // Page<Product> lstProducts = this.productService.getAllProductsWithSpec(pageable, min);

        //case2 max price
        // Double max = maxOptional.isPresent() ? Double.parseDouble(maxOptional.get()) : 100000000;
        // Page<Product> lstProducts = this.productService.getAllProductsWithSpec(pageable, max);

        //case3 factory equal
        // String factory = facOptional.isPresent() ? facOptional.get() : "";
        // Page<Product> lstProducts = this.productService.getAllProductsWithSpec(pageable, factory);

        // case 4 factory 
        // List<String> factory = Arrays.asList(facOptional.get().split(","));
        // Page<Product> lstProducts = this.productService.getAllProductsWithSpec(pageable,factory);

        //case5 min <= price <= max
        // String price = prOptional.isPresent() ? prOptional.get() : "";
        // Page<Product> lstProducts = this.productService.getAllProductsWithSpec(pageable, price);

        //case6
        // List<String> price = Arrays.asList(prOptional.get().split(","));
        // Page<Product> lstProducts = this.productService.getAllProductsWithSpec(pageable,price);
        String qs = request.getQueryString();
        if (qs != null && !qs.isBlank()) {
            // remove page
            qs = qs.replace("page=" + page, "");
        }
        List<Product> product = lstProducts.getContent();
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lstProducts.getTotalPages());
        model.addAttribute("products", product);
        model.addAttribute("queryString", qs);
        return "client/product/show";
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
                                    @RequestParam("receiverPhone") String receiverPhone,
                                    @RequestParam("paymentMethod") String paymentMethod,
                                    @RequestParam("totalPrice") String totalPrice) throws UnsupportedEncodingException{
            HttpSession session = request.getSession(false);
            User user = new User();
            long id = (Long)session.getAttribute("id");
            user.setId(id);
            final String uuid = UUID.randomUUID().toString().replace("-", ""); //truyen len cho vnpay
            this.productService.handlePlaceOrder(user, session, receiverName, receiverAddress, receiverPhone, paymentMethod, uuid);
            if(!paymentMethod.equals("COD")){
                String ip = this.vNPayService.getIpAddress(request);
                String vnpUrl = this.vNPayService.generateVNPayURL(Double.parseDouble(totalPrice), uuid, ip);
    
                return "redirect:" + vnpUrl;
            }
            this.productService.handlePlaceOrder(user, session, receiverName, receiverAddress, receiverPhone, paymentMethod, uuid);
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
    
