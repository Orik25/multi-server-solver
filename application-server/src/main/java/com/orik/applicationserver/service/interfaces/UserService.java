package com.orik.applicationserver.service.interfaces;

import com.orik.applicationserver.DTO.user.UserRegistrationDTO;
import com.orik.applicationserver.entities.User;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);
    User registerUser(UserRegistrationDTO user);
}
