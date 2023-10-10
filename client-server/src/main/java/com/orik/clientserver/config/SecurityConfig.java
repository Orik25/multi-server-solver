package com.orik.clientserver.config;

import com.orik.clientserver.constant.RoleData;
import com.orik.clientserver.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private static final String ADMIN = RoleData.ADMIN.getRoleName();
    private static final String USER = RoleData.USER.getRoleName();
    private static final String VIP = RoleData.VIP.getRoleName();

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/registration").permitAll()
                        .requestMatchers("/system/**").hasRole(ADMIN)
                        .requestMatchers("/api/**").hasAnyRole(VIP, USER)
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        Map<String, String> roleRedirectMap = createRoleRedirectMap();
        return (request, response, authentication) -> {
//           createJWT(authentication);
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                String authority = auth.getAuthority();
                if (roleRedirectMap.containsKey(authority)) {
                    response.sendRedirect(roleRedirectMap.get(authority));
                    return;
                }
            }
        };
    }
    private String createJWT(Authentication authentication) {
        String secretKey = SecurityConstants.JWT_KEY;
        Claims claims = Jwts.claims();
        claims.setSubject(authentication.getName());
        claims.put("role", authentication.getAuthorities());
        claims.put("password", authentication);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private void sendTokenToServers(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8082/setAuthentication";
        HttpEntity<String> requestEntity = new HttpEntity<>(token, headers);

        // Відправляємо POST-запит і отримуємо відповідь
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        System.out.println(responseEntity.getBody());
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8082", "http://localhost:8083"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowCredentials(true);
//        configuration.addAllowedHeader("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    private Map<String, String> createRoleRedirectMap() {
        Map<String, String> roleRedirectMap = new HashMap<>();
        roleRedirectMap.put(RoleData.ADMIN.getDBRoleName(), "/system");
        roleRedirectMap.put(RoleData.USER.getDBRoleName(), "/api");
        roleRedirectMap.put(RoleData.VIP.getDBRoleName(), "/api");
        return roleRedirectMap;
    }
}