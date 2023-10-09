package com.orik.clientserver.controller;

import com.orik.clientserver.DTO.user.UserRegistrationDTO;
import com.orik.clientserver.entities.User;
import com.orik.clientserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping({"/api","/system"})
public class TestController {

    private final UserService userService;
    private final RestTemplate restTemplate;

    @Autowired
    public TestController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
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

    @GetMapping("/to-another/{body}")
    public String getResponse(@PathVariable String body){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Role "+authentication.getAuthorities()); // Додайте ваш токен в заголовок
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String serverUrl = "http://localhost:8082/get-response/"+body;
        ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.GET, entity, String.class);

        String responseBody = response.getBody();

        return responseBody;
    }

}