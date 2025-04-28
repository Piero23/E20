package demacs.unical.esse20.data.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "utente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utente {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 8 massimo 20 caratteri.")
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

}
