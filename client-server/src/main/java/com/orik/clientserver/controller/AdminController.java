package com.orik.clientserver.controller;


import com.orik.clientserver.DTO.request.ServerStatusDTO;
import com.orik.clientserver.constant.Active;
import com.orik.clientserver.constant.JWTTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class AdminController {

    private Map<Integer, ServerStatusDTO> serversStatistic = new HashMap<>();
    private RestTemplate restTemplate;

    @Autowired
    public AdminController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String showAdminPage(){
        return "system";
    }

    @GetMapping("/statistic")
    public Map<Integer, ServerStatusDTO> getStatistic(){
        for (int i = 0; i < Active.servers.length; i++) {
            int port = Active.servers[i];
            String serverUrl = "http://localhost:" + port + "/get-statistic";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer "+ JWTTokenGenerator.generateToken(SecurityContextHolder.getContext().getAuthentication()));
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<ServerStatusDTO> response = restTemplate.exchange(serverUrl, HttpMethod.GET, httpEntity, ServerStatusDTO.class);
            serversStatistic.put(port, response.getBody());
        }
        return serversStatistic;
    }



}
