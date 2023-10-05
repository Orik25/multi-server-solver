package com.orik.applicationserver.service.impl;

import com.orik.applicationserver.DAO.UserRepository;
import com.orik.applicationserver.DTO.user.UserRegistrationDTO;
import com.orik.applicationserver.entities.User;
import com.orik.applicationserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id) //todo: add custom exception
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email) //todo: add custom exception
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public User registerUser(UserRegistrationDTO user) {
//        return userRepository.save(user);//todo: add converter
        return null;
    }

}
