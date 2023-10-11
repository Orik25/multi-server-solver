package com.orik.loadbalancer.controller;

import com.orik.loadbalancer.DTO.ServerStatusDTO;
import com.orik.loadbalancer.constant.Active;
import com.orik.loadbalancer.constant.RoleData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class TestController {

    private final RestTemplate restTemplate;
    private Map<Integer, ServerStatusDTO> serversStatistic = new HashMap<>();
    private int port;
    private int currentTries;
    private final int TRIES = 1;

    @Autowired
    public TestController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.currentTries = 0;
    }

    @GetMapping("/get-port")
    public Integer getStatistic() {
        if(checkVip()){
            return Active.servers[Active.servers.length-1];
        }
        if (currentTries == 0) {
            updateStatistic();
        }
        this.currentTries = (this.currentTries + 1) % TRIES;

        getMinPort();
        return this.port;
    }

    @GetMapping("/update")
    private String update(){
        this.currentTries=0;
        getStatistic();
        return "Statistic updated";
    }

    private void updateStatistic() {
        for (int i = 0; i < Active.servers.length-1; i++) {
            int port = Active.servers[i];
            String serverUrl = "http://localhost:" + port + "/get-statistic";
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            ResponseEntity<ServerStatusDTO> response = restTemplate.exchange(serverUrl, HttpMethod.GET, httpEntity, ServerStatusDTO.class);
            serversStatistic.put(port, response.getBody());
        }
    }

    private boolean checkVip(){
        List<GrantedAuthority> authorities = (List<GrantedAuthority>)SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        return authorities.get(0).toString().equals(RoleData.VIP.getDBRoleName());
    }

    private void getMinPort() {
        Optional<Map.Entry<Integer, ServerStatusDTO>> result = serversStatistic.entrySet().stream()
                .min(Comparator.comparingInt((Map.Entry<Integer, ServerStatusDTO> entry) -> entry.getValue().getActiveThreads())
                        .thenComparingInt(entry -> entry.getValue().getRequestInQueue()));

        if (result.isPresent()) {
            this.port = result.get().getKey();
        } else {
            this.port = Active.servers[0];
        }

    }

}
