//package com.orik.applicationserver.security;
//
//import com.orik.clientserver.entities.User;
//import com.orik.clientserver.service.interfaces.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
////public class CustomUserDetailsService implements UserDetailsService {
//    private final UserService userService;
//
//    @Autowired
//    public CustomUserDetailsService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email)  {
//        User user = userService.findByEmail(email);
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                null,
//                Collections.singleton(authority)
//        );
//    }
//}
