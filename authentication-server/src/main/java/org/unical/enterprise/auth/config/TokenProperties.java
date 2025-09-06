package org.unical.enterprise.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "auth.token")
@Data
public class TokenProperties {

    private String issuer;
    private Long expiration;
    private List<String> scopes;
}
