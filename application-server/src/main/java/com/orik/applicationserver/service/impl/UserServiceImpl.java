package com.orik.applicationserver.service.impl;

import com.orik.applicationserver.DAO.UserRepository;
import com.orik.applicationserver.DTO.user.UserRegistrationDTO;
import com.orik.applicationserver.entities.User;
import com.orik.applicationserver.exception.NoUserFoundException;
import com.orik.applicationserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new NoUserFoundException("User not found with id: " + id));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new NoUserFoundException("User not found with email: " + email));
    }

    @Override
    public User registerUser(UserRegistrationDTO user) {
//        return userRepository.save(user);//todo: add converter
        return null;
    }

}
