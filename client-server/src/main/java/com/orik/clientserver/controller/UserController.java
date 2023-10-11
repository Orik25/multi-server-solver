package com.orik.clientserver.controller;

import com.orik.clientserver.DTO.request.RequestConverterDTO;
import com.orik.clientserver.DTO.request.RequestDTO;
import com.orik.clientserver.DTO.request.StatusRequestDTO;
import com.orik.clientserver.constant.JWTTokenGenerator;
import com.orik.clientserver.entities.Request;
import com.orik.clientserver.entities.User;
import com.orik.clientserver.service.interfaces.RequestService;
import com.orik.clientserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;

@Controller
@RequestMapping("/api")
public class UserController {

    private final RequestService requestService;
    private final RequestConverterDTO requestConverterDTO;
    private final UserService userService;

    private final RestTemplate restTemplate;



    @Autowired
    public UserController(RequestService requestService,
                          RequestConverterDTO requestConverterDTO,
                          UserService userService,
                          RestTemplate restTemplate) {
        this.requestService = requestService;
        this.requestConverterDTO = requestConverterDTO;
        this.userService = userService;
        this.restTemplate = restTemplate;
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
        int port = getPort();
        Request request = requestService.addNew(requestConverterDTO.convertToEntity(index,port));
        String serverUrl = "http://localhost:"+port+"/get-result";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ JWTTokenGenerator.generateToken(SecurityContextHolder.getContext().getAuthentication()));

        RequestDTO requestDTO = requestConverterDTO.convertToDTO(request);

        HttpEntity<RequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);
        ResponseEntity<RequestDTO> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, RequestDTO.class);
        requestService.update(response.getBody());
        return "redirect:/api";
    }

    @GetMapping("/delete-request/{id}")
    public String delete(@PathVariable("id") Long id) {
        requestService.deleteById(id);
        return "redirect:/api";
    }

    @GetMapping("/refresh-request/{id}")
    public String refresh(@PathVariable("id") Long id) {
        Request request = requestService.findById(id);
        int port = request.getPort();
        String serverUrl = "http://localhost:"+port+"/get-status/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ JWTTokenGenerator.generateToken(SecurityContextHolder.getContext().getAuthentication()));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<StatusRequestDTO> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, StatusRequestDTO.class);
        requestService.update(response.getBody());
        return "redirect:/api";
    }

    @GetMapping("/cancel-request/{id}")
    public String cancel(@PathVariable("id") Long id) {
        Request request = requestService.findById(id);
        int port = request.getPort();
        String serverUrl = "http://localhost:"+port+"/cancel-task/"+id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ JWTTokenGenerator.generateToken(SecurityContextHolder.getContext().getAuthentication()));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<StatusRequestDTO> response = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, StatusRequestDTO.class);
        requestService.update(response.getBody());
        return "redirect:/api";
    }

    private Integer getPort(){
        String serverUrl = "http://localhost:8081/get-port";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ JWTTokenGenerator.generateToken(SecurityContextHolder.getContext().getAuthentication()));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer> response = restTemplate.exchange(serverUrl, HttpMethod.GET, requestEntity, Integer.class);
        return response.getBody();
    }
    private User getUserFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(authentication.getName());
    }


}