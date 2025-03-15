package com.trantienloi.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.Cart;
import com.trantienloi.laptopshop.domain.CartDetail;
import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.repository.CartDetailRepository;
import com.trantienloi.laptopshop.repository.CartRepository;
import com.trantienloi.laptopshop.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, 
                          CartDetailRepository cartDetailRepository, 
                          CartRepository cartRepository,
                          UserService userService){
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }
    public List<Product> getAllProducts(){
        return this.productRepository.findAll();
    }
    public Product getProductById(long id){
        return productRepository.findById(id);
    }
    public void deleteProductByID(long id){
         productRepository.deleteById(id);
    }

    //Lay gio hang theo nguoi dung
    public Cart fetchByUser(User user){
        return this.cartRepository.findByUser(user);
    }
    // Thêm từ trang home không phải từ detail
    public void handleAddProductToCart(long id, String email, HttpSession session){
        User user = this.userService.getUserByEmail(email);
        if(user != null){
            Cart cart = this.cartRepository.findByUser(user);  // check xem da co gio hang chua
            if(cart == null){
                // chua co cart -> tao moi Cart
                Cart otherCart = new Cart();
                otherCart.setUser(user);  //set user_id
                otherCart.setSum(0);  //
                cart = this.cartRepository.save(otherCart);  //Luu
            } 
            Product productOptional = this.getProductById(id);// da co gio hang -> them san pham vao detailCart
            if(productOptional != null){
                CartDetail oldDetail = this.cartDetailRepository.findByCartAndProduct(cart, productOptional);
                if(oldDetail == null){
                    CartDetail cd = new CartDetail();
                    cd.setCart(cart);  //luu cart_id cho cartdetail
                    cd.setProduct(productOptional);
                    cd.setPrice(productOptional.getPrice());
                    cd.setQuantity(1);
                    this.cartDetailRepository.save(cd);

                    //Luu lai sum cho cart
                    int s = cart.getSum()+1;
                    cart.setSum(s);
                    cart = this.cartRepository.save(cart);
                    session.setAttribute("sum", s);

                }else{
                    oldDetail.setQuantity(oldDetail.getQuantity()+1);
                    this.cartDetailRepository.save(oldDetail);
                }
            }
        }
    }
    public void handleDeleteCartProduct(long cart_detail_id, HttpSession session){
        Optional<CartDetail> cartDetailOptional = this.cartDetailRepository.findById(cart_detail_id);
        if(cartDetailOptional.isPresent()){
            CartDetail cartDetail = cartDetailOptional.get();
            Cart currentCart = cartDetail.getCart();
            this.cartDetailRepository.deleteById(cart_detail_id);
            if(currentCart.getSum() > 1){
                int s = currentCart.getSum() - 1;
                currentCart.setSum(s);
                session.setAttribute("sum", s);
                this.cartRepository.save(currentCart);
            }
            else{
                this.cartRepository.deleteById(currentCart.getId());
                session.setAttribute("sum", 0);
            }
        }

    }
    public void handleUpdateCartBeforeCheckOut(List<CartDetail> cartDetails){
       for (CartDetail cartDetail : cartDetails) {
        Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
        if (cdOptional.isPresent()) {
            CartDetail currentCartDetail = cdOptional.get();
            currentCartDetail.setQuantity(cartDetail.getQuantity());
            this.cartDetailRepository.save(currentCartDetail);
        }
       }

    }
}
