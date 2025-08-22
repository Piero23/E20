package org.unical.enterprise.authentication;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.unical.enterprise.authentication.dto.RegisterDTORequest;

import java.util.List;

@Service
@RequiredArgsConstructor
//TODO passare tutto il Keyclock admin realm Qui
public class AuthenticationService {

    private Keycloak keycloak;
    private RealmResource realmResource;
    private UsersResource usersResource;

    //TODO Variabili nascoste
    //TODO PULIRE TUTTO MADONNA SANTA CHE SCHIFO

    String serverUrl = "http://localhost:8085";
    String realm = "esse20";
    String username = "admin";
    String password = "admin";
    String clientId = "admin-cli";


    String CLIENT_SECRET;

    public AccessTokenResponse login(String username, String password) {
        Keycloak keycloak2 = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId("esse20-client")
                .clientSecret(CLIENT_SECRET)
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        try {
            AccessTokenResponse tokenResponse = keycloak2.tokenManager().getAccessToken();
            return tokenResponse;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public String registerNewUser(RegisterDTORequest registerDTORequest){


        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        realmResource = keycloak.realm(realm);
        usersResource = realmResource.users();

        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(registerDTORequest.username());
            user.setFirstName(".");
            user.setLastName(".");
            user.setEmail(registerDTORequest.email());
            user.setEnabled(true);

            Response response = usersResource.create(user);
            System.out.println("Status: " + response.getStatus());


            if (response.getStatus() != 201) {
                response.close();
                return "Error: " + response.getStatus();
            }



            String userId = CreatedResponseUtil.getCreatedId(response);
            response.close();

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(registerDTORequest.password());

            UserResource userResource = usersResource.get(userId);
            userResource.resetPassword(credential);

        }catch (Exception e){
            e.printStackTrace();

            return "Error: " + e.getMessage();
        }

        return "201";
    }

    private final RestTemplate restTemplate = new RestTemplate();


    //FIXME  400 Bad Request on POST request for "http://localhost:8085/realms/esse20/protocol/openid-connect/token": "{"error":"invalid_grant","error_description":"Invalid refresh token. Token client and authorized client don't match"}"
    public AccessTokenResponse refreshAccessToken(String refreshToken) {
        String tokenUrl = serverUrl
                + "/realms/" + realm
                + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String,String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "refresh_token");
        form.add("client_id", clientId);
        form.add("client_secret", "T8v2mi3WWV5JxXOJSCDn7KGDdspKcKvz");
        form.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<AccessTokenResponse> response = restTemplate
                .exchange(tokenUrl, HttpMethod.POST, request, AccessTokenResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to refresh token: " + response.getStatusCode());
        }

        return response.getBody();
    }

    //TODO
    public void logout(){

    }


    public void assignClientRoleToUser(String username, String clientId, String roleName) {

        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl("http://localhost:8085")
                .realm("esse20")
                .clientId("admin-cli")
                .username("admin")
                .password("admin")
                .build();

        RealmResource realmResource = keycloak.realm("esse20");
        UsersResource usersResource = realmResource.users();

        List<UserRepresentation> users = usersResource.search(username);
        if (users.isEmpty()) {
            throw new RuntimeException("Utente non trovato: " + username);
        }
        String userId = users.get(0).getId();
        UserResource userResource = usersResource.get(userId);


        List<ClientRepresentation> clients = realmResource.clients().findByClientId(clientId);
        if (clients.isEmpty()) {
            throw new RuntimeException("Client non trovato: " + clientId);
        }
        String clientUUID = clients.get(0).getId();


        RoleRepresentation clientRole = realmResource.clients()
                .get(clientUUID)
                .roles()
                .get(roleName)
                .toRepresentation();


        userResource.roles()
                .clientLevel(clientUUID)
                .add(List.of(clientRole));
    }
}
