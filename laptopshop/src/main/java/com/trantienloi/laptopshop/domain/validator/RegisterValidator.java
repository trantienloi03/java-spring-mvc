package com.trantienloi.laptopshop.domain.validator;

import org.springframework.stereotype.Service;

import com.trantienloi.laptopshop.domain.dto.registerUser;
import com.trantienloi.laptopshop.service.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Service
public class RegisterValidator implements ConstraintValidator<RegisterChecked, registerUser> {
    private final UserService userService;
    public RegisterValidator(UserService userService){
        this.userService = userService;
    }
    @Override
    public boolean isValid(registerUser user, ConstraintValidatorContext context) {
        boolean valid = true;
        //check firstName
        if (user.getFirstName().length() == 0) {
            context.buildConstraintViolationWithTemplate("Vuii lòng nhập first name")
                    .addPropertyNode("firstName")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
         //check lastName
         if (user.getLastName().length() == 0) {
            context.buildConstraintViolationWithTemplate("Vui lòng nhập last name")
                    .addPropertyNode("lastName")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
        //check lastName
        if (user.getEmail().length() == 0) {
            context.buildConstraintViolationWithTemplate("Vui lòng nhập Emaiil")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
        // Check if password fields match
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            context.buildConstraintViolationWithTemplate("Nhập lại mật khẩu không chính xác")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }
         //check exist email
         if (this.userService.checkExistEmail(user.getEmail())) {
            context.buildConstraintViolationWithTemplate("Email đã tồn tại")
                    .addPropertyNode("email")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            valid = false;
        }

        // Additional validations can be added here

        return valid;
    }
}
