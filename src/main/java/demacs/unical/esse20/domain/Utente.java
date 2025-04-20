package demacs.unical.esse20.domain;

import jakarta.persistence.*;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import org.hibernate.annotations.UuidGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "utente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 8 massimo 20 caratteri.")
    private String username;

    @Column(nullable = false, unique = false)
    @Email
    private String email;

    @Column(unique = true, nullable = false)
    private String password;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Enumerated(EnumType.STRING)
    private Ruolo ruolo;

}
