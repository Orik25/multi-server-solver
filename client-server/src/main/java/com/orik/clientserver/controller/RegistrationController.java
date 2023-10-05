package com.orik.clientserver.controller;

import com.orik.clientserver.DTO.user.UserRegistrationDTO;
import com.orik.clientserver.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "registration";
    }

    @PostMapping
    public String registerUser(@Valid @ModelAttribute("user") UserRegistrationDTO user,
                               BindingResult theBindingResult) {
        if (theBindingResult.hasErrors()) {
            return "registration";
        } else {
            userService.registerUser(user);
            return "redirect:/login?successRegister=true";
        }
    }
}
