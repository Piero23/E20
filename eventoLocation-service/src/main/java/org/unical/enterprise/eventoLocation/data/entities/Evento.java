package org.unical.enterprise.eventoLocation.data.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.unical.enterprise.shared.dto.EventoBasicDto;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "evento")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Evento extends DomainObject<Long> {

    public Evento(String descrizione, Location location , String nome, UUID organizzatore, Long posti, boolean b_riutilizzabile, boolean b_nominativo, boolean a_r, Double prezzo) {
        this.descrizione = descrizione;
        this.location = location;
        this.organizzatore = organizzatore;
        this.nome = nome;
        this.posti = posti;
        this.b_riutilizzabile = b_riutilizzabile;
        this.b_nominativo = b_nominativo;
        this.prezzo = prezzo;
        this.age_restricted = a_r;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 500)
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Location location;

    @Column(nullable = false, length = 36)
    private UUID organizzatore;

    @Column(nullable = false)
    private String nome;

    @Column
    private Long posti;

    @Column(nullable = false)
    private boolean b_riutilizzabile;

    @Column(nullable = false)
    private boolean b_nominativo;

    @Column(nullable = false)
    private boolean age_restricted;

    @Column(nullable = false)
    private Date data;

    @Column(nullable = false)
    private double prezzo;

    // to-DTO Conversion
    public static EventoBasicDto toSharedDTO(Evento evento) {
        return EventoBasicDto.builder()
                .id(evento.getId())
                .nome(evento.getNome())
                .descrizione(evento.getDescrizione())
                .organizzatore(evento.getOrganizzatore())
                .posti(evento.getPosti())
                .b_riutilizzabile(evento.isB_riutilizzabile())
                .b_nominativo(evento.isB_nominativo())
                .locationId(evento.getLocation().getId())
                .data(evento.getData())
                .prezzo(evento.getPrezzo())
                .age_restricted(evento.isAge_restricted())
                .build();
    }
}
