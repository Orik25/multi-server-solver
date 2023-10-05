package com.orik.clientserver.service.interfaces;

import com.orik.clientserver.DTO.user.UserRegistrationDTO;
import com.orik.clientserver.entities.User;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    User registerUser(UserRegistrationDTO user);
}
