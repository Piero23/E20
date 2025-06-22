package org.unical.enterprise.shared.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.shared.dto.MailTransferDto;

@FeignClient(name = "mailSender-service")
public interface MailServiceClient {

    @PostMapping(value = "/api/mail/sendMail", consumes = "application/json")
    void sendMail(@RequestBody MailTransferDto dto);

    @GetMapping("api/mail/test")
    String test();
}
