package com.orik.applicationserver.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class AdminController {
    @GetMapping
    public String showAdminPage(){
        return "system";
    }

}
