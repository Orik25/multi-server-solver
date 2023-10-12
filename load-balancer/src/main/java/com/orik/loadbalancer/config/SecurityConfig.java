package com.orik.loadbalancer.config;



import com.orik.loadbalancer.constant.RoleData;
import com.orik.loadbalancer.filter.JWTTokenValidatorFilter;
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
    private static final String ADMIN = RoleData.ADMIN.getRoleName();
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/get-port","/update").hasAnyRole(USER,VIP)
                        .requestMatchers("/get-statistic").hasRole(ADMIN)
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


}
