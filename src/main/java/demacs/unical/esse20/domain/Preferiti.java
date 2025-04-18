package demacs.unical.esse20.domain;

import jakarta.persistence.*;
import jdk.jfr.Event;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preferiti",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_utente_evento",
                        columnNames = { "utente_id", "evento_id" }
                )
        })
@NoArgsConstructor

public class Preferiti {

    public Preferiti(String utente_id, Evento evento, boolean status) {
        this.utente_id = utente_id;
        this.evento = evento;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 36 ,nullable = false)
    private String utente_id;

    @ManyToOne
    @JoinColumn
    private Evento evento;

    // Privato/Condiviso [0,1]
    @Column(nullable = false)
    private boolean status;
}
