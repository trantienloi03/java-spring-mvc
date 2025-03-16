package com.trantienloi.laptopshop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.Cart;
import com.trantienloi.laptopshop.domain.CartDetail;
import com.trantienloi.laptopshop.domain.Order;
import com.trantienloi.laptopshop.domain.OrderDetail;
import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.repository.CartDetailRepository;
import com.trantienloi.laptopshop.repository.CartRepository;
import com.trantienloi.laptopshop.repository.OrderDetailRepository;
import com.trantienloi.laptopshop.repository.OrderRepository;
import com.trantienloi.laptopshop.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, 
                          CartDetailRepository cartDetailRepository, 
                          CartRepository cartRepository,
                          UserService userService,
                          OrderRepository orderRepository,
                          OrderDetailRepository orderDetailRepository ){
        this.productRepository = productRepository;
        this.cartDetailRepository = cartDetailRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }
    public Page<Product> getAllProducts(Pageable pageable){
        return this.productRepository.findAll(pageable);
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
    public void handleAddProductToCart(long id, String email, HttpSession session, long quantity){
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
                    cd.setQuantity(quantity);
                    this.cartDetailRepository.save(cd);

                    //Luu lai sum cho cart
                    int s = cart.getSum()+1;
                    cart.setSum(s);
                    cart = this.cartRepository.save(cart);
                    session.setAttribute("sum", s);

                }else{
                    oldDetail.setQuantity(oldDetail.getQuantity()+quantity);
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
    public void handleUpdateCartBeforeCheckout(List<CartDetail> cartDetails){
       for (CartDetail cartDetail : cartDetails) {
        Optional<CartDetail> cdOptional = this.cartDetailRepository.findById(cartDetail.getId());
        if (cdOptional.isPresent()) {
            CartDetail currentCartDetail = cdOptional.get();
            currentCartDetail.setQuantity(cartDetail.getQuantity());
            this.cartDetailRepository.save(currentCartDetail);
        }
       }

    }
    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress, String receiverPhone){
        // create order
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        order.setStatus("PENDING");
        // order = this.orderRepository.save(order);
        Cart cart = this.cartRepository.findByUser(user);
        double sum = 0;
        for (CartDetail cd : cart.getCartDetails()) {
            sum += cd.getPrice() * cd.getQuantity();
        }
        order.setTotalPrice(sum);
        order = this.orderRepository.save(order);
        // create orderDetail

        // step 1: get cart by user
      
        if (cart != null) {
            List<CartDetail> cartDetails = cart.getCartDetails();

            if (cartDetails != null) {
                for (CartDetail cd : cartDetails) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrder(order);
                    orderDetail.setProduct(cd.getProduct());
                    orderDetail.setPrice(cd.getPrice());
                    orderDetail.setQuantity(cd.getQuantity());

                    this.orderDetailRepository.save(orderDetail);
                }

                // step 2: delete cart_detail and cart
                for (CartDetail cd : cartDetails) {
                    this.cartDetailRepository.deleteById(cd.getId());
                }

                this.cartRepository.deleteById(cart.getId());

                // step 3 : update session
                session.setAttribute("sum", 0);
            }
        }
    }
}
