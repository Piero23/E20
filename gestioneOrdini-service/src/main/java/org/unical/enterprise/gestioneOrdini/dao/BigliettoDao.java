package org.unical.enterprise.gestioneOrdini.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.unical.enterprise.gestioneOrdini.domain.Biglietto;
import org.unical.enterprise.gestioneOrdini.domain.Ordine;

import java.util.List;
import java.util.UUID;

public interface BigliettoDao extends JpaRepository<Biglietto, UUID> {

    List<Biglietto> findAllByOrdine(Ordine ordine);

    @Query("SELECT COUNT(b) > 0 FROM Biglietto b WHERE b.email = :email AND b.idEvento = :idEvento")
    boolean existsByEmailAndIdEvento(@Param("email") String email, @Param("idEvento") Long idEvento);

    List<Biglietto> findAllByIdEvento(Long idEvento);

}
