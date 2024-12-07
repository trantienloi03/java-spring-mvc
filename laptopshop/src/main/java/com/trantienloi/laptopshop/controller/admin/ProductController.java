package com.trantienloi.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.trantienloi.laptopshop.domain.Product;
import com.trantienloi.laptopshop.service.ProductService;
import com.trantienloi.laptopshop.service.UploadFileService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;




@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadFileService uploadFileService;
    public ProductController(ProductService productService,UploadFileService uploadFileService ){
        this.productService = productService;
        this.uploadFileService = uploadFileService;
    }
    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> lstProducts = this.productService.getAllProducts();
        for (Product product : lstProducts) {
            System.out.println(product.toString());
        }
        model.addAttribute("lstProducts", lstProducts);
        return "admin/product/show";
    }
    @GetMapping("/admin/product/{id}")
    public String getDetailProduct(Model model, @PathVariable long id){
        Product detailProduct = this.productService.getProductById(id);
        model.addAttribute("product", detailProduct);
        return "admin/product/detail";
    }
    @GetMapping("/admin/product/create")
    public String getPageCreate(Model model) {
        model.addAttribute("NewProduct", new Product());
        return "admin/product/create";
    }
    @PostMapping("/admin/product/create")
    public String createProduct(Model model, 
                                @Valid @ModelAttribute("NewProduct") Product newProduct,
                                BindingResult newProducBindingResult,
                                @RequestParam("trantienloiFile") MultipartFile file ) {

        List<FieldError> errors = newProducBindingResult.getFieldErrors();
        for (FieldError fieldError : errors) {
            System.out.println(">>>>>"+ fieldError.getField()+":"+ fieldError.getDefaultMessage());
        }
        if(newProducBindingResult.hasErrors()){
            return "admin/product/create";
        }
        String image = this.uploadFileService.handleSaveUploadFile(file, "product");
        newProduct.setImage(image);
        this.productService.saveProduct(newProduct);
        return "redirect:/admin/product";
    }
    
    
    
}
