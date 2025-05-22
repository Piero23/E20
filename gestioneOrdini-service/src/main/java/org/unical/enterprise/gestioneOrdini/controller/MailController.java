package org.unical.enterprise.gestioneOrdini.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unical.enterprise.gestioneOrdini.service.BigliettoService;
import org.unical.enterprise.gestioneOrdini.service.MailService;
import org.unical.enterprise.gestioneOrdini.service.OrdineService;

import java.util.UUID;

@RestController
@RequestMapping("/api/mail")
@AllArgsConstructor
public class MailController {

    //Questo schifo esiste unicamente a fini di testing e verrà probabilmente nuclearizzato in futuro

    MailService mailService;
    BigliettoService bigliettoService;
    OrdineService ordineService;

    @PostMapping("/{email}")
    public void sendQrCodeMail(@PathVariable String email) {
        mailService.sendQrCodeMail(email, bigliettoService.getQrCode(new UUID(0, 0)));
    }

    @PostMapping("/mail/{email}")
    public void sendMail(@PathVariable String email) {
        mailService.sendMail(email, ordineService.findById(UUID.fromString("7b702b46-8400-41ce-ab7f-cf17ac555604")));
    }
}
