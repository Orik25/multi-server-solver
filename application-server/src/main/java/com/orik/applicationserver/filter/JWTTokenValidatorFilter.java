package com.orik.applicationserver.filter;

import com.orik.applicationserver.constant.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JWTTokenValidatorFilter  extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (StringUtils.hasText(token)) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.JWT_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                List<GrantedAuthority> authorities = new ArrayList<>();
                List<Map<String, String>> roles = (List<Map<String, String>>) claims.get("role");

                for (Map<String, String> role : roles) {
                    String roleName = role.get("authority");
                    authorities.add(new SimpleGrantedAuthority(roleName));
                }
                if (StringUtils.hasText(username)) {
                    Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                            authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    throw new BadCredentialsException("Invalid token: Missing username");
                }
            } catch (ExpiredJwtException e) {
                throw new CredentialsExpiredException("Token has expired", e);
            } catch (JwtException | IllegalArgumentException e) {
                throw new BadCredentialsException("Invalid token", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

}
