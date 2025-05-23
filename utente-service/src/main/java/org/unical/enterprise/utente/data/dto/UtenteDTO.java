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
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteDTO {
    private UUID id;

    @NotBlank(message = "Il campo username non può essere vuoto.")
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 4, massimo 20 caratteri.")
    private String username;

    @NotBlank(message = "Il campo email non può essere vuoto.")
    @Email(message = "L'indirizzo email non è valido.")
    private String email;

    @NotNull(message = "La data di nascita è obbligatoria.")
    private LocalDate data_nascita;
}
