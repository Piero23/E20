package demacs.unical.esse20.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.sql.results.graph.Fetch;

import java.sql.Blob;
import java.util.*;

@Entity
@Table(name = "utente")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Utente{

    public Utente(String username, String email, boolean organizzatore, String password, Date data_nascita) {
        this.username = username;
        this.email = email;
        this.organizzatore = organizzatore;
        this.password = password;
        this.data_nascita = data_nascita;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean organizzatore;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Date data_nascita;

    /*@OneToMany(fetch = FetchType.LAZY)
    private Set<Evento> eventi_organizzati = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "utente_amici",
            joinColumns = @JoinColumn(name = "utente_id"),
            inverseJoinColumns = @JoinColumn(name = "amico_id")
    )
    private Set<Utente> amici = new HashSet<>();

     */


}
