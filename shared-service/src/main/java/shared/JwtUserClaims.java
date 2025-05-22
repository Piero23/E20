package shared;

import java.util.List;

/*
 * DTO che contiene username e ruoli estratti dal token JWT.
 */
public class JwtUserClaims {
    private String username;
    private List<String> authorities;

    public JwtUserClaims(String username, List<String> authorities) {
        this.username = username;
        this.authorities = authorities;
    }

    public JwtUserClaims() {
    }

    public String getUsername() {
        return username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}