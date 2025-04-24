package demacs.unical.esse20.data.dto;

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

    @NotBlank(message = "Lo username non può essere vuoto.")
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 4, massimo 20 caratteri.")
    private String username;

    @NotBlank(message = "L'email non può essere vuota.")
    @Email(message = "L'email non è valida.")
    private String email;

    private boolean organizzatore;

    // private String password; excluded for security reasons

    @NotNull(message = "La data di nascita è obbligatoria.")
    private LocalDate data_nascita;
}
