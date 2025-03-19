package com.trantienloi.laptopshop.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.Cart;
import com.trantienloi.laptopshop.domain.CartDetail;
import com.trantienloi.laptopshop.domain.Order;
import com.trantienloi.laptopshop.domain.OrderDetail;
import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.domain.dto.ProductCriteriaDTO;
import com.trantienloi.laptopshop.repository.CartDetailRepository;
import com.trantienloi.laptopshop.repository.CartRepository;
import com.trantienloi.laptopshop.repository.OrderDetailRepository;
import com.trantienloi.laptopshop.repository.OrderRepository;
import com.trantienloi.laptopshop.repository.ProductRepository;
import com.trantienloi.laptopshop.service.Specification.ProductSpecs;

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

    //case0 namelike
    public Page<Product> getAllProductsWithSpec(ProductCriteriaDTO productCriteriaDTO ,Pageable pageable){
        Specification<Product> combinedSpec = Specification.where(null);
        if(productCriteriaDTO.getFactory() == null
            && productCriteriaDTO.getPrice() == null
            && productCriteriaDTO.getTarget() == null
            && productCriteriaDTO.getPrice() == null)
            {
                return this.productRepository.findAll(pageable);
            }
        if( productCriteriaDTO.getFactory() != null && productCriteriaDTO.getFactory().isPresent()){
            Specification<Product> facSpec = ProductSpecs.matchListFactory(productCriteriaDTO.getFactory().get());
            combinedSpec = combinedSpec.and(facSpec);
        }
        if(productCriteriaDTO.getTarget() != null && productCriteriaDTO.getTarget().isPresent() ){
            Specification<Product> tarSpec = ProductSpecs.matchListTarget(productCriteriaDTO.getTarget().get());
            combinedSpec = combinedSpec.and(tarSpec);
        }
        if(productCriteriaDTO.getPrice() != null && productCriteriaDTO.getPrice().isPresent() ){
            Specification<Product> tarSpec = this.BuildPriceSpec(pageable,productCriteriaDTO.getPrice().get());
            combinedSpec = combinedSpec.and(tarSpec);
        }
        return this.productRepository.findAll(combinedSpec, pageable);
    }


    //case1 min
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, Double min){
    //     return this.productRepository.findAll(ProductSpecs.minPrice(min), pageable);
    // }


    //case2 max
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, Double max){
    //     return this.productRepository.findAll(ProductSpecs.maxPrice(max), pageable);
    // }

    //case3 factory equal
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, String factory){
    //     if(factory.equals(""))
    //         return this.productRepository.findAll(pageable);
    //     else
    //         return this.productRepository.findAll(ProductSpecs.matchFactory(factory), pageable);
    // }

    //case4 List factory
    // public Page<Product> getAllProductsWithSpec(Pageable pageable, List<String> factory){
    //     return this.productRepository.findAll(ProductSpecs.matchListFactory(factory), pageable);
    // }

    //case5 
    // public Page<Product> getAllProductsWithSpec(Pageable page, String price){
    //      // eg: price 10-toi-15-trieu
    //      if (price.equals("10-toi-15-trieu")) {
    //         double min = 10000000;
    //         double max = 15000000;
    //         return this.productRepository.findAll(ProductSpecs.matchPrice(min, max),
    //                 page);

    //     } else if (price.equals("15-toi-30-trieu")) {
    //         double min = 15000000;
    //         double max = 30000000;
    //         return this.productRepository.findAll(ProductSpecs.matchPrice(min, max),
    //                 page);
    //     } else
    //         return this.productRepository.findAll(page);
    // }

    //case 6 
    public Specification<Product> BuildPriceSpec(Pageable pageable, List<String> price){
        Specification<Product> combinedSpec = (root, query, criteriaBuilder) -> criteriaBuilder.disjunction();
        for (String p : price) {
            double min = 0;
            double max = 0;

            // Set the appropriate min and max based on the price range string
            switch (p) {
                case "duoi-10-trieu":
                    min = 1;
                    max = 10000000;
                    break;
                case "10-15-trieu":
                    min = 10000000;
                    max = 15000000;
                    break;
                case "15-20-trieu":
                    min = 15000000;
                    max = 20000000;
                    break;
                case "tren-20-trieu":
                    min = 20000000;
                    max = 200000000;
                    break;
                // Add more cases as needed
            }

            if (min != 0 && max != 0) {
                Specification<Product> rangeSpec = ProductSpecs.matchListPrice(min, max);
                combinedSpec = combinedSpec.or(rangeSpec);
            }
        }
        return combinedSpec;
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
    public void handlePlaceOrder(User user, HttpSession session, String receiverName, String receiverAddress, String receiverPhone, String paymentMethod, String uuid){
        // create order
        Order order = new Order();
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("PAYMENT_UNPAID");
        // final String uuid = UUID.randomUUID().toString().replace("-", "");
        order.setPaymentRef(!paymentMethod.equals("COD") ? uuid : "UNKNOWN");
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
