package com.orik.applicationserver.config;


import com.orik.applicationserver.constant.RoleData;
import com.orik.applicationserver.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private static final String USER = RoleData.USER.getRoleName();
    private static final String VIP = RoleData.VIP.getRoleName();
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/get-status/**","/get-result","/cancel-task/**").hasAnyRole(USER,VIP)
                        .requestMatchers("/get-statistic").permitAll()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


}
