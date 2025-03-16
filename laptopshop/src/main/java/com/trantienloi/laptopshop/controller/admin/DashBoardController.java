package com.trantienloi.laptopshop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.trantienloi.laptopshop.service.UserService;

@Controller
public class DashBoardController {
    private final UserService userService;
    public DashBoardController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/admin")
    public String getDashBoard(Model model){
        model.addAttribute("countUsers", this.userService.countUser());
        model.addAttribute("countProducts", this.userService.countProducts());
        model.addAttribute("countOrders", this.userService.countOrders());
        return"admin/dashboard/show";
    }
}
