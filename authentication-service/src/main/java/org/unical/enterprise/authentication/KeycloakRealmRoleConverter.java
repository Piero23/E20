package org.unical.enterprise.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final String CLIENT_ID = "ae-client";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null || !resourceAccess.containsKey(CLIENT_ID)) return List.of();

        Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get(CLIENT_ID);
        List<String> roles = (List<String>) clientRoles.get("roles");

        if (roles == null) return List.of();

        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            System.out.println(role);
        }

        return authorities;
    }
}