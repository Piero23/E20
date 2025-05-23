package org.unical.enterprise.authentication;



import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.client.RestTemplate;
import org.unical.enterprise.authentication.dto.LoginDTORequest;
import org.unical.enterprise.authentication.dto.LoginDTOResponse;
import org.unical.enterprise.authentication.dto.RegisterDTORequest;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    //TODO HttpResponse consono allo standard

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDTORequest loginDTORequest) {

        return new ResponseEntity<>(KeycloackAdminRealm.getInstance().login(loginDTORequest.username(),loginDTORequest.password()), HttpStatus.OK);
    }

    @GetMapping("/test")
    private String test() {
        return "Fatto";
    }

    @PostMapping("/registerBasic")
    public String register(@RequestBody RegisterDTORequest registerDTORequest) {
        KeycloackAdminRealm.getInstance().registerNewUser(registerDTORequest);
        KeycloackAdminRealm.getInstance().assignClientRoleToUser(registerDTORequest.username(),"esse20-client","USER");
        return registerDTORequest.toString();
    }
}
