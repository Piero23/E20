package org.unical.enterprise.gateway;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gateway")
public class TempController {

    @GetMapping("/me")
    String gatewayTest(Authentication auth) {
        return "Sono il GateWay. Ciao " + auth.getName();
    }

    @GetMapping("/whatIs")
    String gatewayTokenTest(Authentication auth) {
        return auth.getPrincipal().toString();
    }
}
