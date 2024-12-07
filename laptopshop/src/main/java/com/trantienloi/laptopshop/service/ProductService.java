package com.trantienloi.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.repository.ProductRepository;
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
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
}
