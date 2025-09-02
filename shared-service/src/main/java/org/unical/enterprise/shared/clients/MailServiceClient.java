package org.unical.enterprise.shared.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.unical.enterprise.shared.dto.MailTransferDto;

@FeignClient(name = "mailSender-service", configuration = FeignConfig.class)
public interface MailServiceClient {

    @PostMapping(value = "/api/mail/sendMail", consumes = "application/json")
    void sendMail(@RequestBody MailTransferDto dto);

    @GetMapping("api/mail/test")
    String test();
}
