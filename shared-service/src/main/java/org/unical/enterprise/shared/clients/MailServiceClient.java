package org.unical.enterprise.shared.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.shared.dto.MailTransferDto;

@FeignClient(name = "mailSender-service", configuration = FeignConfig.class)
public interface MailServiceClient {

    @PostMapping(value = "/api/mail/sendMail", consumes = "application/json")
    void sendMail(@RequestBody MailTransferDto dto);

    @PostMapping("/api/mail/{email}")
    void sendQrCodeMail(@PathVariable String email, @RequestBody String image);

    @GetMapping("api/mail/test")
    String test();
}
