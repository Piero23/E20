package org.unical.enterprise.eventoLocation.data.dao;

import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventoDao extends JpaRepository<Evento, Long> {

    Page<Evento> findAll(Pageable pageable);
}
