package demacs.unical.esse20;

import demacs.unical.esse20.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDao extends JpaRepository<Location, Long> {
}
