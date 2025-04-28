package demacs.unical.esse20.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteRegistrationDTO {
    @NotBlank(message = "Lo username è obbligatorio.")
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 4, massimo 20 caratteri.")
    private String username;

    @NotBlank(message = "L'email è obbligatoria.")
    @Email(message = "L'email non è valida.")
    private String email;

    @NotBlank(message = "La password è obbligatoria.")
    @Size(min = 8, max = 100, message = "La password deve contenere almeno 8 caratteri.")
    private String password;

    @NotNull(message = "La data di nascita è obbligatoria.")
    private LocalDate dataNascita;
}
