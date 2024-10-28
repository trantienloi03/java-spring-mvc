package com.trantienloi.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.service.UserService;
import java.util.List;

@Controller
public class AdminControlller {
    private UserService userService;
    
    public AdminControlller(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        model.addAttribute("NewUser", new User());
        return "admin/user/table-user";
    }
    @RequestMapping("/admin/user/create")
    public String CreateUser(Model model) {
        model.addAttribute("NewUser", new User());
        return "admin/user/create";
    }
    @RequestMapping(value = "/admin/user/create", method=RequestMethod.POST)
    public String Index(Model model, @ModelAttribute("NewUser") User TranTienLoi) {
        System.out.println("Du lieu tu view la: "+TranTienLoi);
        userService.handleSaveUser(TranTienLoi);
        return "hello";
    }
}
