package demacs.unical.esse20.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "evento")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private Long location_id;

    @OneToOne(mappedBy = "evento", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Utente organizzatore;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Long posti;

    //Immagine

    @Column(nullable = false)
    private boolean b_riutilizzabile;

    @Column(nullable = false)
    private boolean b_nominativo;
}
