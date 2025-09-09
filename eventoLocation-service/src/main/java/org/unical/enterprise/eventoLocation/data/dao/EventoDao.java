package org.unical.enterprise.eventoLocation.data.dao;

import org.unical.enterprise.eventoLocation.data.entities.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface EventoDao extends JpaRepository<Evento, Long> {

    Page<Evento> findAll(Pageable pageable);
    Page<Evento> findAllByNomeContainingIgnoreCase(String nome, Pageable pageable);

    List<Evento> findAllByOrganizzatore(UUID organizzatore);
}
