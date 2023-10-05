package com.orik.applicationserver.DTO.user;

import com.orik.applicationserver.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class UserRegistrationDTO {

    @Email
    @UniqueEmail
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String firstName;

    private String lastName;

    @Pattern(message = "Must contain at least 1 capital letter, at least 1 number, at least 8 characters",
            regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$")
    private String password;

    @NotNull(message = "Type is mandatory")
    private Boolean isVip;

    public UserRegistrationDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(Boolean vip) {
        isVip = vip;
    }
}