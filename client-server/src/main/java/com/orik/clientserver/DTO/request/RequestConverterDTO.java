package com.orik.clientserver.DTO.request;

import com.orik.clientserver.constant.RequestStatus;
import com.orik.clientserver.entities.Request;
import com.orik.clientserver.entities.User;
import com.orik.clientserver.service.interfaces.RequestService;
import com.orik.clientserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class RequestConverterDTO {
    private final RequestService requestService;
    private final UserService userService;


    public RequestConverterDTO(RequestService requestService,UserService userService) {
        this.requestService = requestService;
        this.userService = userService;
    }

    public Request convertToEntity(int index){
        Request request = new Request();
        request.setRequest(index);
        request.setStatus(RequestStatus.IN_PROGRESS.getStatus());
        request.setTime(ZonedDateTime.now());
        request.setUser(getUserFromAuthentication());
        return request;
    }

    private User getUserFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(authentication.getName());
    }
}
