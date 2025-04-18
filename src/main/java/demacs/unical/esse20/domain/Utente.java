package demacs.unical.esse20.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.sql.results.graph.Fetch;
import org.hibernate.validator.constraints.UUID;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "utente")
@Setter
@Getter
@NoArgsConstructor
@ToString
@RequiredArgsConstructor
public class Utente {

    public Utente(String username, String email, boolean organizzatore, String password, LocalDate data_nascita) {
        this.username = username;
        this.email = email;
        this.organizzatore = organizzatore;
        this.password = password;
        this.data_nascita = data_nascita;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    @Size(min = 4, max = 20, message = "Lo username deve contenere almeno 8 massimo 20 caratteri.")
    private String username;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private boolean organizzatore;

    @Column(length = 500, unique = true, nullable = false)
    @Size(min = 8, max = 32, message = "La password deve contenere almeno 8 massimo 32 caratteri.")
    private String password;

    @Column(nullable = false)
    private LocalDate data_nascita;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "organizzatore")
    private Set<Evento> eventi_organizzati = new HashSet<>();


    /*
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "utente_amici",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "amico_id")
    )
    private Set<Utente> amici = new HashSet<>();
     */

}
