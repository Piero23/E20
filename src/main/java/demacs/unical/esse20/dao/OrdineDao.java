package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrdineDao extends JpaRepository<Ordine, UUID> {

    List<Ordine> findAllByUtenteId(UUID utente_id);
}
