package org.unical.enterprise.eventoLocation.data.entities;

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

    public Preferiti(String utenteId, Evento evento) {
        this.utenteId = utenteId;
        this.evento = evento;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 36, max = 36)
    @Column(length = 36 ,nullable = false)
    private String utenteId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Evento evento;



}
