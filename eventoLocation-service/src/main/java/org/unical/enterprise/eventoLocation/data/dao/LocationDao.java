package org.unical.enterprise.eventoLocation.data.dao;

import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.unical.enterprise.eventoLocation.data.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationDao extends JpaRepository<Location, Long> {
    Page<Location> findAll(Pageable pageable);

    Page<Location> findAllByNomeContainingIgnoreCase(String string, Pageable pageable);
}
