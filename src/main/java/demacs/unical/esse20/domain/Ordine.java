package demacs.unical.esse20.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.*;

@Entity
@Table(name = "ordine")
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Ordine {

    public Ordine(String utenteId, int biglietti_comprati, double importo, Date data_pagamento) {
        this.utenteId = utenteId;
        this.biglietti_comprati = biglietti_comprati;
        this.importo = importo;
        this.data_pagamento = data_pagamento;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 36)
    private String utenteId;

    @Column(nullable = false)
    private int biglietti_comprati;

    @Column(nullable = false)
    private double importo;

    @Column(nullable = false)
    private Date data_pagamento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ordine", orphanRemoval = true,fetch = FetchType.LAZY)
    Set<Biglietto> biglietti = new HashSet<>();

    public boolean addBiglietto(Biglietto biglietto) {
        try {
            biglietti.add(biglietto);
            biglietto.setOrdine(this);

            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}