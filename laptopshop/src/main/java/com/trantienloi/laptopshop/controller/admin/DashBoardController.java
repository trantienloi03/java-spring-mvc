package com.trantienloi.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashBoardController {
    @GetMapping("/admin")
    public String getDashBoard(){
        return"admin/dashboard/show";
    }
    @GetMapping("/admin/product")
    public String getProduct(){
        return"admin/product/show";
    }
    @GetMapping("/admin/order")
    public String getOrder(){
        return"admin/order/show";
    }
}
