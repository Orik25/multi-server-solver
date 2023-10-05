package com.orik.clientserver.service.impl;

import com.orik.clientserver.DAO.UserRepository;
import com.orik.clientserver.DTO.user.UserConverterDTO;
import com.orik.clientserver.DTO.user.UserRegistrationDTO;
import com.orik.clientserver.entities.User;
import com.orik.clientserver.exception.NoUserFoundException;
import com.orik.clientserver.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserConverterDTO userConverter;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverterDTO userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
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
        return userRepository.save(userConverter.convertToEntity(user));
    }

}
