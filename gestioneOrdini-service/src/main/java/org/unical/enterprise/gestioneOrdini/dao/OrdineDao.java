package org.unical.enterprise.gestioneOrdini.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;

import java.util.List;
import java.util.UUID;

public interface OrdineDao extends JpaRepository<Ordine, UUID> {

    List<Ordine> findAllByUtenteId(UUID utente_id);
}
