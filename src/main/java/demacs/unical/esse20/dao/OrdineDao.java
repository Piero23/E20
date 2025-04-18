package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdineDao extends JpaRepository<Ordine, String> {

    List<Ordine> findAllByUtenteId(String utente_id);
}
