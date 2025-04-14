package demacs.unical.esse20;

import demacs.unical.esse20.domain.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoDao extends JpaRepository<Evento, Long> {
}
