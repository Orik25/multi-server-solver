package com.orik.clientserver.controller;

import com.orik.clientserver.service.interfaces.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserController {

    private final RequestService requestService;

    @Autowired
    public UserController(RequestService requestService) {
        this.requestService = requestService;
    }


    @GetMapping
    public String getUsers(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "1") int size,
                           @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                           @RequestParam(name = "sortOrder", defaultValue = "asc") String sortOrder) {
        model.addAttribute("historyPage", requestService.getAllRequestsSorted(page,size,sortField, sortOrder));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        return "user/user-page";
    }

    @GetMapping("/delete-request/{id}")
    public String delete(@PathVariable("id") Long id) {
        requestService.deleteById(id);
        return "redirect:/api";
    }
}
