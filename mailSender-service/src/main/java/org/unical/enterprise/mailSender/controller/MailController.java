package org.unical.enterprise.mailSender.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.mailSender.service.MailService;


import java.awt.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/mail")
@AllArgsConstructor
public class MailController {

    MailService mailService;

    @PostMapping("/{email}")
    public void sendQrCodeMail(@PathVariable String email) {
        mailService.sendQrCodeMail(email);
    }

    @PostMapping("/mail/{email}")
    public void sendMail(@PathVariable String email) {
        mailService.sendMail(email);
    }

    @GetMapping("/test")
    private String test() {
        return "Sono MailController";
    }
}
