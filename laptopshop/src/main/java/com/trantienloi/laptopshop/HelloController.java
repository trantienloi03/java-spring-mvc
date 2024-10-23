package com.trantienloi.laptopshop;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HelloController {
    @GetMapping("/")
    public String getMethodName() {
        return "Hello Word, update1223";
    }
    @GetMapping("/user")
    public String UserPage() {
        return "Only user can access this page, update";
    }
    @GetMapping("/admin")
    public String AdminPage() {
        return "Only admin can access this page";
    }
    
    
}
