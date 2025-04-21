package demacs.unical.esse20.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "preferiti",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_utente_evento",
                        columnNames = { "utente_id", "evento_id" }
                )
        })
@NoArgsConstructor
@Getter
@Setter
public class Preferiti extends DomainObject<Long> {

    public Preferiti(String utente_id, Evento evento, boolean status) {
        this.utente_id = utente_id;
        this.evento = evento;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 36, max = 36)
    @Column(length = 36 ,nullable = false)
    private String utente_id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Evento evento;

    // Privato/Condiviso [0,1]
    @Column(nullable = false)
    private boolean status;
}
