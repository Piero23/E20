package org.unical.enterprise.shared.dto;

import lombok.Getter;
import lombok.Setter;

//TODO Hardcodare gli url?
@Getter
@Setter
public class CheckoutRequest {
    private String successUrl;
    private String cancelUrl;
    private OrdineTransferDto ordine;
}
