//package com.orik.applicationserver.security;
//
//import com.orik.clientserver.exception.CustomAuthenticationException;
//import com.orik.clientserver.exception.NoUserFoundException;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthenticationProvider2 implements AuthenticationManager {
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//
//        try {
//            return new UsernamePasswordAuthenticationToken(authentication, authentication.getAuthorities());
//        } catch (NoUserFoundException e) {
//            throw new CustomAuthenticationException("Authentication failed: " + e.getMessage());
//        }
//    }
//}
