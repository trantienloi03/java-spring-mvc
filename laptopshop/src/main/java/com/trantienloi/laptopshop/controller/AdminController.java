package com.trantienloi.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.service.UserService;
import java.util.List;

@Controller
public class AdminController {
    private UserService userService;
    
    public AdminController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> lstUsers = userService.getAllUsers();
        model.addAttribute("Model_users", lstUsers);
        return "admin/user/table-user";
    }
    @RequestMapping("/admin/user/{id}")
    public String getDetailUserPage(Model model, @PathVariable long id) {
        User tienloi = userService.getUserById(id);
        model.addAttribute("user",tienloi);
        return "admin/user/show";
    }
    @RequestMapping("/admin/user/update/{id}")
    public String updateUser(Model model, @PathVariable long id) {
        User currentUser = userService.getUserById(id);
        model.addAttribute("currentUser",currentUser);
        return "admin/user/update";
    }
    @RequestMapping("/admin/user/create")
    public String CreateUser(Model model) {
        model.addAttribute("NewUser", new User());
        return "admin/user/create"; // file
    }
    @RequestMapping(value = "/admin/user/update", method=RequestMethod.POST)
    public String update(Model model, @ModelAttribute("currentUser") User currentUser) {
        User currennt = userService.getUserById(currentUser.getId());
        if(currennt != null){
            currennt.setFullname(currentUser.getFullname());
            currennt.setAddress(currentUser.getAddress());
            currennt.setPhone(currentUser.getPhone());
            this.userService.handleSaveUser(currennt);
        }
        userService.handleSaveUser(currentUser);
        return "redirect:/admin/user"; // trả lại url != file
    }
    @RequestMapping(value = "/admin/user/create", method=RequestMethod.POST)
    public String Index(Model model, @ModelAttribute("NewUser") User TranTienLoi) {
        userService.handleSaveUser(TranTienLoi);
        return "redirect:/admin/user"; // trả lại url != file
    }
}
