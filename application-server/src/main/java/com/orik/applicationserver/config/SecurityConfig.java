package com.orik.applicationserver.config;

import com.orik.applicationserver.constant.RoleData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;

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
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/login", "/registration").permitAll()
                        .requestMatchers("/system/**").hasRole(ADMIN)
                        .requestMatchers("/api/**").hasAnyRole(VIP, USER)
                )
                .formLogin((form) -> form
//                        .loginPage("/login")
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
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                String authority = auth.getAuthority();
                if (roleRedirectMap.containsKey(authority)) {
                    response.sendRedirect(roleRedirectMap.get(authority));
                    return;
                }
            }
        };
    }

    private Map<String, String> createRoleRedirectMap() {
        Map<String, String> roleRedirectMap = new HashMap<>();
        roleRedirectMap.put(RoleData.ADMIN.getDBRoleName(), "/system");
        roleRedirectMap.put(RoleData.USER.getDBRoleName(), "/api");
        roleRedirectMap.put(RoleData.VIP.getDBRoleName(), "/api");
        return roleRedirectMap;
    }
}