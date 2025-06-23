package org.unical.enterprise.shared.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import static java.lang.System.out;
/*
 * Intercetta ogni richiesta HTTP, estrae e valida il token JWT dall'header Authorization e autentica l'utente.
 */

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        String internalHeader = request.getHeader("X-Internal-Request");

        if ("true".equals(internalHeader)) {
            UsernamePasswordAuthenticationToken internalAuth =
                    new UsernamePasswordAuthenticationToken("internal-service", null, List.of(
                            new SimpleGrantedAuthority("ROLE_ADMIN"),
                            new SimpleGrantedAuthority("ROLE_USER"),
                            new SimpleGrantedAuthority("ROLE_MANAGER")
                    ));
            SecurityContextHolder.getContext().setAuthentication(internalAuth);
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        
        try {
            // Estrai le credenziali dal token
            JwtUserClaims userClaims = jwtService.extractUserClaims(jwt);
            
            if (userClaims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Crea UserDetails dalla rappresentazione delle authorities nel JWT
                UserDetails userDetails = JwtUserDetails.fromUserClaims(userClaims);


                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}