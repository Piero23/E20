package org.unical.enterprise.mailSender.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.mailSender.service.MailService;
import org.unical.enterprise.shared.dto.MailTransferDto;

@RestController
@RequestMapping("/api/mail")
@AllArgsConstructor
public class MailController {

    MailService mailService;

    @PostMapping("/{email}")
    public void sendQrCodeMail(@PathVariable String email) {
        mailService.sendQrCodeMail(email);
    }

    @PostMapping(value = "/sendMail", consumes = "application/json")
    public void sendMail(@RequestBody MailTransferDto mailTransferDto) {
        mailService.sendMail(mailTransferDto);
    }

    @GetMapping("/test")
    private String test() {
        return "Sono MailController";
    }
}
