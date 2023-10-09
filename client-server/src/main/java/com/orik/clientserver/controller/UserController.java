package com.orik.clientserver.controller;

import com.orik.clientserver.DTO.request.RequestConverterDTO;
import com.orik.clientserver.entities.Request;
import com.orik.clientserver.entities.User;
import com.orik.clientserver.service.interfaces.RequestService;
import com.orik.clientserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserController {

    private final RequestService requestService;
    private final RequestConverterDTO requestConverterDTO;
    private final UserService userService;


    @Autowired
    public UserController(RequestService requestService, RequestConverterDTO requestConverterDTO, UserService userService) {
        this.requestService = requestService;
        this.requestConverterDTO = requestConverterDTO;
        this.userService = userService;
    }


    @GetMapping
    public String getUsers(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "4") int size,
                           @RequestParam(name = "sortField", defaultValue = "id") String sortField,
                           @RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder) {
        model.addAttribute("historyPage", requestService.getAllRequestsSorted(page,size,sortField, sortOrder, getUserFromAuthentication().getId()));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortOrder);
        return "user/user-page";
    }

    @PostMapping("/find-number")
    public String findFibonacciNumber(@RequestParam("request") int index) {
        requestService.addNew(requestConverterDTO.convertToEntity(index));
        return "redirect:/api";
    }

    @GetMapping("/delete-request/{id}")
    public String delete(@PathVariable("id") Long id) {
        requestService.deleteById(id);
        return "redirect:/api";
    }
    private User getUserFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(authentication.getName());
    }
}
