package org.unical.enterprise.gestioneOrdini;


import org.springframework.cloud.openfeign.FeignClient;

//TODO Metterlo nello shared
@FeignClient(name = "utente-service")
public interface UtenteServiceClient {

}
