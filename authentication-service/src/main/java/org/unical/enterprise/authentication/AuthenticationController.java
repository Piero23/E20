package org.unical.enterprise.authentication;


import lombok.AllArgsConstructor;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.authentication.dto.LoginDTORequest;
import org.unical.enterprise.authentication.dto.RegisterDTORequest;

@RestController
@RequestMapping("/api/authentication")
@AllArgsConstructor
public class AuthenticationController {

    //TODO HttpResponse consono allo standard

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDTORequest loginDTORequest) {
        AccessTokenResponse tokens = authenticationService
                .login(loginDTORequest.username(),loginDTORequest.password());
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/test")
    public AccessTokenResponse test(@RequestBody String ref) {

        System.out.println(ref);
        return authenticationService.refreshAccessToken(ref);
    }

    @GetMapping("/test1")
    public String test() {
        return "Funzia";
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTORequest registerDTORequest) {

        try {
            authenticationService.registerNewUser(registerDTORequest);
            authenticationService.assignClientRoleToUser(registerDTORequest.username(), "esse20-client", "USER");
            return ResponseEntity.ok("role assigned");
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("error");
        }
    }
}
