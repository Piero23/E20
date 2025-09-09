package org.unical.enterprise.payment.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/web")
public class WebController {

    @GetMapping("/success")
    public String orderSuccess(
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) String customerEmail,
            Model model) {

        model.addAttribute("eventName",
                Optional.ofNullable(eventName).orElse("l'evento selezionato"));
        model.addAttribute("customerEmail",
                Optional.ofNullable(customerEmail).orElse("la tua email"));

        return "success";
    }

    @GetMapping("/fail")
    public String orderFailure(
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) String customerEmail,
            Model model) {

        model.addAttribute("eventName",
                Optional.ofNullable(eventName).orElse("l'evento selezionato"));
        model.addAttribute("customerEmail",
                Optional.ofNullable(customerEmail).orElse("la tua email"));

        return "fail";
    }
}
