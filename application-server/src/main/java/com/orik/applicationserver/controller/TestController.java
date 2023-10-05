package com.orik.applicationserver.controller;

import com.orik.applicationserver.DTO.user.UserRegistrationDTO;
import com.orik.applicationserver.entities.User;
import com.orik.applicationserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    private final UserService userService;

    @Autowired
    public TestController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/users")
//    public User getRole(){
//        return roleRepository.findById(1L)
//                .orElseThrow(NullPointerException::new);
//    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id){
        return userService.findById(id);
    }
    @PostMapping("/users")
    public User getUserById(@RequestBody UserRegistrationDTO id){
        return userService.registerUser(id);
    }
}