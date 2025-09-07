package org.unical.enterprise.mailSender.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.unical.enterprise.mailSender.service.MailService;
import org.unical.enterprise.shared.dto.MailTransferDto;
import org.unical.enterprise.shared.dto.TicketMailDTO;

@RestController
@RequestMapping("/api/mail")
@AllArgsConstructor
public class MailController {

    MailService mailService;

    @PostMapping("/{email}")
    public void sendQrCodeMail(@PathVariable String email, @RequestBody TicketMailDTO ticketMailDTO) {
        mailService.sendQrCodeMail(email, ticketMailDTO);
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
