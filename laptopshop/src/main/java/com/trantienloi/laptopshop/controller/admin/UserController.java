package com.trantienloi.laptopshop.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;


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
        Pageable pageable = PageRequest.of(0, 4);
        Page<User> lstUsers = userService.getAllUsers(pageable);
        System.out.println(lstUsers);
        List<User> lstUsersByEmail = userService.getUsersByEmail("Abc@gmail.com");
        System.out.println(lstUsersByEmail);
        model.addAttribute("TienLoi", test);
        model.addAttribute("name", "Tran tien Loi");
        return "hello";
    }
    @RequestMapping("/admin/user")
    public String getUserPage(Model model, @RequestParam("page") Optional<String> OptionalPage) {
        int page = 1;
        try {
            if(OptionalPage.isPresent()){
                page = Integer.parseInt(OptionalPage.get());
            }
            else{
                //
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        Pageable pageable = PageRequest.of(page-1, 4);
        Page<User> lstUsers = userService.getAllUsers(pageable);
        List<User> lst = lstUsers.getContent();
        model.addAttribute("Model_users", lst);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", lstUsers.getTotalPages());
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
        User current = userService.getUserById(currentUser.getId());
        if(current != null){
            if(file.isEmpty() == false){
                String img = this.uploadFileService.handleSaveUploadFile(file, "avatar");
                current.setAvatar(img);
            }
            current.setFullname(currentUser.getFullname());
            current.setAddress(currentUser.getAddress());
            current.setPhone(currentUser.getPhone());
            current.setRole(this.userService.getRoleByName(currentUser.getRole().getName()));
            this.userService.handleSaveUser(current);
        }
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
                                @Valid @ModelAttribute("NewUser") User TranTienLoi,
                                BindingResult newUSerBindingResult,
                                @RequestParam("trantienloiFile") MultipartFile file) 
    {
         List<FieldError> errors = newUSerBindingResult.getFieldErrors();     
         for (FieldError fieldError : errors) {
            System.out.println(">>>>>>>>" + fieldError.getField()+", "+ fieldError.getDefaultMessage());
         }       
         if(newUSerBindingResult.hasErrors()){
            return "admin/user/create";
         }          
            
        String avatar = this.uploadFileService.handleSaveUploadFile(file, "avatar");
        String hashPassword = this.passwordEncoder.encode(TranTienLoi.getPassword());
        TranTienLoi.setAvatar(avatar);
        TranTienLoi.setPassword(hashPassword);
        TranTienLoi.setRole(this.userService.getRoleByName(TranTienLoi.getRole().getName()));
        userService.handleSaveUser(TranTienLoi);
        return "redirect:/admin/user"; // trả lại url != file
    }
    
    
}

