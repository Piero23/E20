package org.unical.enterprise.auth.data.dto;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Lo Username è obbligatorio.")
    @Size(min = 4, max = 20, message = "Lo Username deve contenere almeno 4 caratteri, massimo 20.")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
             message = "Lo Username può contenere solo lettere maiuscole e minuscole, numeri, punti, trattini ed underscore")
    private String username;

    @NotBlank(message = "L'indirizzo email è obbligatorio.")
    @Email(message = "L'indirizzo email non è valido.")
    private String email;

    @NotBlank(message = "La password è obbligatoria.")
    @Size(min = 8, max = 64, message = "La password deve contenere almeno 8 caratteri, massimo 64.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "La Password deve contenere almeno una lettera maiscola, una lettera minuscola, " +
                    "un numero ed un carattere speciale tra questi: @ $ ! % * ? &"
    )
    private String password;

    @NotNull(message = "La data di nascita è obbligatoria.")
    private LocalDate dataNascita;
}
