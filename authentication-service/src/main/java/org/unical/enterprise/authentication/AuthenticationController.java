package org.unical.enterprise.authentication;



import org.keycloak.admin.client.Keycloak;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;
import org.unical.enterprise.authentication.dto.RegisterDTORequest;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    Keycloak keycloak = Keycloak.getInstance(
            "http://localhost:8085",
            "esse20",
            "admin",
            "admin",
            "esse20-client");
    //RealmRepresentation realm = keycloak.realm("esse20").toRepresentation();

    @GetMapping("/login")
    public HttpEntity<?> login() {
        try {
            URL url = new URL("http://localhost:8085/realms/esse20/protocol/openid-connect/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // Costruisci la query string con parametri urlencoded
            StringBuilder params = new StringBuilder();
            params.append("grant_type=").append(URLEncoder.encode("password", "UTF-8"));
            params.append("&client_id=").append(URLEncoder.encode("esse20-client", "UTF-8"));
            params.append("&client_secret=").append(URLEncoder.encode("xYFpNBla8svc7yZna4eArJQRhAh4jgMh", "UTF-8"));
            params.append("&username=").append(URLEncoder.encode("test", "UTF-8"));
            params.append("&password=").append(URLEncoder.encode("testpassword", "UTF-8"));

            // Scrivi i dati nel body della richiesta
            try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                dos.writeBytes(params.toString());
                dos.flush();
            }

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? conn.getInputStream() : conn.getErrorStream()
            ));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @PostMapping("/register")
    private String register(@RequestBody RegisterDTORequest registerDTORequest) {

        RestTemplate restTemplate = new RestTemplate();
        URI url = URI.create("http://localhost:8085/admin/realms/esse20/users");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJRb1hNV0NmXzVJZGRpc1N2WHMwNk5Zam9DN1ltZFZqM1NjX0hlcHl4bVhZIn0.eyJleHAiOjE3NDc5MzkyMjAsImlhdCI6MTc0NzkzODkyMCwianRpIjoib25ydHJvOjRhOTFmMWVlLThlZGUtNDg5ZS1iYTcwLWE1MWZjZGQ5OTcyOSIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4NS9yZWFsbXMvZXNzZTIwIiwiYXVkIjpbInJlYWxtLW1hbmFnZW1lbnQiLCJhY2NvdW50Il0sInN1YiI6ImU2YTU1ZDA2LTI5MTYtNGI4Ny05OTBkLTcyNzhkY2ViM2RjZSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImVzc2UyMC1jbGllbnQiLCJzaWQiOiI3Y2ExOTg4Yi03M2QxLTQyNGItOGI3Ni05OWM1MmM1ZDEyNWIiLCJhY3IiOiIxIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm9mZmxpbmVfYWNjZXNzIiwiTUFOQUdFUiIsIkFETUlOIiwidW1hX2F1dGhvcml6YXRpb24iLCJVU0VSIiwiZGVmYXVsdC1yb2xlcy1lc3NlMjAiXX0sInJlc291cmNlX2FjY2VzcyI6eyJyZWFsbS1tYW5hZ2VtZW50Ijp7InJvbGVzIjpbInZpZXctaWRlbnRpdHktcHJvdmlkZXJzIiwidmlldy1yZWFsbSIsIm1hbmFnZS1pZGVudGl0eS1wcm92aWRlcnMiLCJpbXBlcnNvbmF0aW9uIiwicmVhbG0tYWRtaW4iLCJjcmVhdGUtY2xpZW50IiwibWFuYWdlLXVzZXJzIiwicXVlcnktcmVhbG1zIiwidmlldy1hdXRob3JpemF0aW9uIiwicXVlcnktY2xpZW50cyIsInF1ZXJ5LXVzZXJzIiwibWFuYWdlLWV2ZW50cyIsIm1hbmFnZS1yZWFsbSIsInZpZXctZXZlbnRzIiwidmlldy11c2VycyIsInZpZXctY2xpZW50cyIsIm1hbmFnZS1hdXRob3JpemF0aW9uIiwibWFuYWdlLWNsaWVudHMiLCJxdWVyeS1ncm91cHMiXX0sImVzc2UyMC1jbGllbnQiOnsicm9sZXMiOlsiQURNSU4iXX0sImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFtZSI6InRlc3QgdGVzdGVyc29uIiwicHJlZmVycmVkX3VzZXJuYW1lIjoidGVzdCIsImdpdmVuX25hbWUiOiJ0ZXN0IiwiZmFtaWx5X25hbWUiOiJ0ZXN0ZXJzb24iLCJlbWFpbCI6InRlc3RAbWFpbC5jb20ifQ.XQJy9vNdO7g80wUD5WiV-uFqp5zesoi5l11kK8PGyDShFqAPD5CfMBuvrmcRo-CUpH9OkLMFDe9_88p7nY5Oc9kwaOMAv92iFXKibmvB1Pn9do8387xa7f6w2h1mrf_7LqiB_dOoO65fKjyRFFjp6bcgyj1HM68fcWEReBxKPs-uVqEXqQ9Up-nKSQBNTPY_4HV_1P519rKDs9p00iHIw0nQGtCiqPQDiiBHzWZC_xuM-kKzawBCv_rX-mg4d_1joCc2uEkUKiFK4nFE_eqj9-n9vcFanGlwrfnjxwPC-GrAtY4VamBLiS7u23OOw_WiysO61klUv3V529vXhITEpQ");
        // Costruzione del corpo JSON
        Map<String, Object> user = new HashMap<>();
        user.put("username", registerDTORequest.username());
        user.put("enabled", true);
        user.put("email", registerDTORequest.email());
        user.put("emailVerified", false);
        user.put("firstName", "Mario");
        user.put("lastName", "Rossi");

        // Password (credential)
        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");
        credential.put("value", registerDTORequest.password());
        credential.put("temporary", false);

        user.put("credentials", List.of(credential));

        // Attributi custom
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("attribute_key", "attribute_value");
        user.put("attributes", attributes);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        return null;
    }

    @GetMapping("/test")
    private String test() {
        return keycloak.toString();
    }
}
