package com.trantienloi.laptopshop.controller.admin;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;

import com.trantienloi.laptopshop.domain.User;
import com.trantienloi.laptopshop.service.UploadFileService;
import com.trantienloi.laptopshop.service.UserService;

import java.util.List;


@Controller
public class UserController {
    private final UserService userService;
    private final UploadFileService uploadFileService;
    private final PasswordEncoder passwordEncoder;
        // DI: dependency injection
        public UserController(UserService userService, UploadFileService uploadFileService, PasswordEncoder passwordEncoder) {
            this.userService = userService;
            this.uploadFileService = uploadFileService;
            this.passwordEncoder = passwordEncoder;
        }
    @RequestMapping("/")
    public String getHomePage(Model model) {
        String test = this.userService.handldeHello();
        List<User> lstUsers = userService.getAllUsers();
        System.out.println(lstUsers);
        List<User> lstUsersByEmail = userService.getUsersByEmail("Abc@gmail.com");
        System.out.println(lstUsersByEmail);
        model.addAttribute("TienLoi", test);
        model.addAttribute("name", "Tran tien Loi");
        return "hello";
    }
    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> lstUsers = userService.getAllUsers();
        model.addAttribute("Model_users", lstUsers);
        return "admin/user/show";
    }
    @RequestMapping("/admin/user/{id}")
    public String getDetailUserPage(Model model, @PathVariable long id) {
        User tienloi = userService.getUserById(id);
        model.addAttribute("user",tienloi);
        return "admin/user/detail";
    }
    @RequestMapping("/admin/user/update/{id}")
    public String updateUser(Model model, @PathVariable long id) {
        User currentUser = userService.getUserById(id);
        model.addAttribute("currentUser",currentUser);
        return "admin/user/update";
    }
   
    @PostMapping("/admin/user/update")
    public String update(Model model, @ModelAttribute("currentUser") User currentUser,@RequestParam("trantienloiFile") MultipartFile file) {
        User currennt = userService.getUserById(currentUser.getId());
        if(currennt != null){
            currennt.setFullname(currentUser.getFullname());
            String hashCode = this.passwordEncoder.encode(currentUser.getPassword());
            currennt.setPassword(hashCode);
            currennt.setAddress(currentUser.getAddress());
            currennt.setPhone(currentUser.getPhone());
            String avatar = this.uploadFileService.handleSaveUploadFile(file, "avatar");
            currennt.setAvatar(avatar);
            currennt.setRole(this.userService.getRoleByName(currentUser.getRole().getName()));
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
    
    @GetMapping("/admin/user/create")
    public String getCreateUserPage(Model model) {
        model.addAttribute("NewUser", new User());
        return "admin/user/create"; // file
    }
    @PostMapping("/admin/user/create")
    public String createUserPage(Model model, 
                                @ModelAttribute("NewUser") User TranTienLoi,
                                @RequestParam("trantienloiFile") MultipartFile file) 
    {
                                     
            
        String avatar = this.uploadFileService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(TranTienLoi.getPassword());
        TranTienLoi.setAvatar(avatar);
        TranTienLoi.setPassword(hashPassword);
        TranTienLoi.setRole(this.userService.getRoleByName(TranTienLoi.getRole().getName()));
        userService.handleSaveUser(TranTienLoi);
        return "redirect:/admin/user"; // trả lại url != file
    }
    
    
}

