//package com.orik.applicationserver.security;
//
//import com.orik.clientserver.exception.CustomAuthenticationException;
//import com.orik.clientserver.exception.NoUserFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomAuthenticationProvider implements AuthenticationProvider {
//
//    private CustomUserDetailsService userDetailsService;
//
//
//    @Autowired
//    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws CustomAuthenticationException {
//        String email = authentication.getName();
//        String password = authentication.getCredentials().toString();
//
//
//        try {
//            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
//        } catch (NoUserFoundException e) {
//            throw new CustomAuthenticationException("Authentication failed: " + e.getMessage());
//        }
//    }
//
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}