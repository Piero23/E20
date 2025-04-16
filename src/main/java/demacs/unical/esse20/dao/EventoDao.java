package demacs.unical.esse20.dao;

import demacs.unical.esse20.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoDao extends JpaRepository<Evento, Long> {
}
