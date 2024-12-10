package com.trantienloi.laptopshop.domain.dto;

import com.trantienloi.laptopshop.domain.validator.RegisterChecked;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@RegisterChecked
public class registerUser {
    @NotNull
    @Size(min=3, message = "Vui lòng nhập tối thiểu 3 ký tự")
    private String firstName;

    @NotNull
    @Size(min=3, message = "Vui lòng nhập tối thiểu 3 ký tự")
    private String lastName;

    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email không được bỏ trống")
    private String email;

    @NotNull
    @NotEmpty(message = "Không được bỏ trống!")
    private String password;
    
    @NotNull
    @NotEmpty(message = "Không được bỏ trống!")
    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    
}
