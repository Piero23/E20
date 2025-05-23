package org.unical.enterprise.gestioneOrdini.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;

import java.util.List;
import java.util.UUID;

public interface BigliettoDao extends JpaRepository<Biglietto, UUID> {

    List<Biglietto> findAllByOrdine(Ordine ordine);
}
