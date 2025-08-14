package org.unical.enterprise.shared.security;

public class ProtectedRoute {
    private String path;
    private String[] roles;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}

