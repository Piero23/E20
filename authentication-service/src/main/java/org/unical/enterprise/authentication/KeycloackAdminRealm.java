package org.unical.enterprise.authentication;

import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.unical.enterprise.authentication.dto.RegisterDTORequest;

import java.util.List;


public class KeycloackAdminRealm {

    //TODO Rilevamento eccezzioni

    private Keycloak keycloak;
    private RealmResource realmResource;
    private UsersResource usersResource;

    //TODO Variabili nascoste
    String serverUrl = "http://localhost:8085";
    String realm = "esse20";
    String username = "admin";
    String password = "admin";
    String clientId = "admin-cli";

    private KeycloackAdminRealm() {

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
    }

    private static KeycloackAdminRealm instance;

    public static KeycloackAdminRealm getInstance() {
        if(instance!=null){
            return instance;
        }

        instance = new KeycloackAdminRealm();
        return instance;
    }


    public String registerNewUser(RegisterDTORequest registerDTORequest){

        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(registerDTORequest.username());
            user.setFirstName(".");
            user.setLastName(".");
            user.setEmail(registerDTORequest.email());
            user.setEnabled(true);

            // Crea l'utente
            Response response = usersResource.create(user);
            System.out.println("Status: " + response.getStatus());


            if (response.getStatus() != 201) {
                response.close();
                return "Error: " + response.getStatus();
            }


            // Estrai l'ID utente dalla location dell'header
            String userId = CreatedResponseUtil.getCreatedId(response);
            response.close();

            // Imposta la password
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(registerDTORequest.password());

            UserResource userResource = usersResource.get(userId);
            userResource.resetPassword(credential);

        }catch (Exception e){
            e.printStackTrace();

            return "Error";
        }

        return "201";
    }

    public String login(String username, String password){
        // Build Keycloak instance
        Keycloak keycloak2 = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId("esse20-client")
                .clientSecret("T8v2mi3WWV5JxXOJSCDn7KGDdspKcKvz")
                .username(username)
                .password(password)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        try {
            return keycloak2.tokenManager().getAccessToken().getToken();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public boolean close(){
        try {
            keycloak.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
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
