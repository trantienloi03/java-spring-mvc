package com.trantienloi.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.service.UserService;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
   
    @PostMapping("/admin/user/update")
    public String update(Model model, @ModelAttribute("currentUser") User currentUser) {
        User currennt = userService.getUserById(currentUser.getId());
        if(currennt != null){
            currennt.setFullname(currentUser.getFullname());
            currennt.setAddress(currentUser.getAddress());
            currennt.setPhone(currentUser.getPhone());
            this.userService.handleSaveUser(currennt);
        }
        userService.handleSaveUser(currennt);
        return "redirect:/admin/user"; // trả lại url != file
    }
    @GetMapping("/admin/user/delete/{id}")
    public String DeleteUser(Model model, @PathVariable long id) {
        model.addAttribute("id", id);
        User user = new User();
        user = this.userService.getUserById(id);
        user.setId(id);
        model.addAttribute("UserDelete", user);
        return "admin/user/delete";
    }
    @PostMapping("/admin/user/delete")
    public String PostDeleteUser(@ModelAttribute("UserDelete") User user) {
        //TODO: process POST request
        this.userService.DeleteUserByID(user.getId());
        return  "redirect:/admin/user";
    }
    
    @RequestMapping("/admin/user/create")
    public String CreateUser(Model model) {
        model.addAttribute("NewUser", new User());
        return "admin/user/create"; // file
    }
    @RequestMapping(value = "/admin/user/create", method=RequestMethod.POST)
    public String Index(Model model, @ModelAttribute("NewUser") User TranTienLoi) {
        userService.handleSaveUser(TranTienLoi);
        return "redirect:/admin/user"; // trả lại url != file
    }
}
