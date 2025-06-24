package org.unical.enterprise.gestioneOrdini.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "ordine")
@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UuidGenerator
    @Column(length = 36)
    private UUID id;

    @Column(nullable = false, length = 36)
    private UUID utenteId;

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