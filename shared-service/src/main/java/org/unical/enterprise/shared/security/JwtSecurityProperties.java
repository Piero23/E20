package org.unical.enterprise.shared.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/*
 * Definisce le proprietà configurabili (percorsi pubblici, rotte protette, flags) per la sicurezza di ogni servizio.
 */
@ConfigurationProperties(prefix = "jwt.security")
public class JwtSecurityProperties {

    private boolean enabled = true;
    private boolean openService = false;
    private String[] publicPaths;
    private List<ProtectedRoute> protectedRoutes= new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOpenService() {
        return openService;
    }

    public void setOpenService(boolean openService) {
        this.openService = openService;
    }

    public String[] getPublicPaths() {
        return publicPaths;
    }

    public void setPublicPaths(String[] publicPaths) {
        this.publicPaths = publicPaths;
    }

    public List<ProtectedRoute> getProtectedRoutes() {
        return protectedRoutes;
    }

    public void setProtectedRoutes(List<ProtectedRoute> protectedRoutes) {
        this.protectedRoutes = protectedRoutes;
    }
}
