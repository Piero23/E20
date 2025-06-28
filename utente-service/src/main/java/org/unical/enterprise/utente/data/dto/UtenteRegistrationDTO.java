package org.unical.enterprise.utente.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteRegistrationDTO {
    @NotBlank(message = "Lo username è obbligatorio.")
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 4, massimo 20 caratteri.")
    private String username;

    @NotBlank(message = "L'indirizzo email è obbligatorio.")
    @Email(message = "L'indirizzo email non è valido.")
    private String email;

    @NotBlank(message = "La password è obbligatoria.")
    @Size(min = 8, max = 100, message = "La password deve contenere almeno 8 caratteri.")
    private String password;

    @NotNull(message = "La data di nascita è obbligatoria.")
    private LocalDate dataNascita;
}
