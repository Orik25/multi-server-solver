package com.orik.clientserver.controller;

import com.orik.clientserver.DTO.user.UserRegistrationDTO;
import com.orik.clientserver.constant.JWTTokenGenerator;
import com.orik.clientserver.constant.SecurityConstants;
import com.orik.clientserver.entities.User;
import com.orik.clientserver.service.interfaces.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    @GetMapping("/get-response")
    public String get(){
        RestTemplate restTemplate = new RestTemplate();

        String serverUrl = "http://localhost:8082/get-from-app";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+JWTTokenGenerator.generateToken(SecurityContextHolder.getContext().getAuthentication()));

        HttpEntity<?> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.GET, httpEntity, String.class);

        String responseBody = response.getBody();
        return responseBody;
    }


}