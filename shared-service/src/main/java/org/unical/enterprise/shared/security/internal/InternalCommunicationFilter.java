package org.unical.enterprise.shared.security.internal;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalCommunicationFilter extends OncePerRequestFilter {

    @Value("${internal.communication-key}")
    private String internalCommunicationKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String internalHeader = request.getHeader("X-Internal-Communication");

        System.out.println("Sto controllando la comm Key");

        if (internalHeader == null || !internalHeader.equals(internalCommunicationKey)) {
            System.out.println("Ci hai provato");
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        System.out.println("OK, puoi passare");

        filterChain.doFilter(request, response);

    }

}
