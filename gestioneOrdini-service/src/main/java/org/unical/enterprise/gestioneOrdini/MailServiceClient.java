package org.unical.enterprise.gestioneOrdini;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("mailSender-service")
public interface MailServiceClient {

    @PostMapping("/mail/{email}")
    void sendMail(@PathVariable String email);
}
