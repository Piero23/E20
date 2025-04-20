package demacs.unical.esse20.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "utenti")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 8 massimo 20 caratteri.")
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(unique = true, nullable = false)
    @Size(min = 8, max = 32, message = "La password deve contenere almeno 8 massimo 32 caratteri.")
    private String password;

    @Column(name = "data_di_nascita", nullable = false)
    private LocalDate dataDiNascita;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

}
