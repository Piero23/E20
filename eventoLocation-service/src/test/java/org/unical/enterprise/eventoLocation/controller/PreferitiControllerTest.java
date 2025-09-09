package org.unical.enterprise.eventoLocation.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.unical.enterprise.eventoLocation.service.PreferitiService;
import org.unical.enterprise.eventoLocation.service.EventoService;
import org.unical.enterprise.eventoLocation.data.dto.PreferitiDto;
import org.unical.enterprise.shared.dto.EventoBasicDto;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

class PreferitiControllerTest {

    @Mock
    private PreferitiService preferitiService;

    @Mock
    private EventoService eventoService;

    private PreferitiController preferitiController;
    private List<EventoBasicDto> mockEventi;

    private List<PreferitiDto> mockPreferiti;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        preferitiController = new PreferitiController(preferitiService, eventoService);

        mockEventi = Arrays.asList(
                createMockEvento(1L, "Evento 1"),
                createMockEvento(2L, "Evento 2")
        );

        mockPreferiti = Arrays.asList(
                createMockPreferitiDto(1L, "utente1" ,1L),
                createMockPreferitiDto(2L, "utente2" , 1L),
                createMockPreferitiDto(3L, "utente1" ,2L)
        );
    }

    @Test
    void testGetAllPreferiti() {

        String utenteId = "user123";
        when(preferitiService.getAllEventiByUtenteId(utenteId)).thenReturn(mockEventi);

        ResponseEntity<List<EventoBasicDto>> response = preferitiController.getAllPreferiti(utenteId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals("Evento 1", response.getBody().get(0).getNome());
        assertEquals(2L, response.getBody().get(1).getId());
        assertEquals("Evento 2", response.getBody().get(1).getNome());
    }

    @Test
    void testAggiungiAiPreferiti_Success() {

        UUID utenteId = UUID.randomUUID();
        Long eventoId = 1L;
        String internal = "true";


        ResponseEntity<?> response = preferitiController.aggiungiAiPreferiti(internal, utenteId, eventoId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(preferitiService).save(any(PreferitiDto.class));
    }

    @Test
    void testAggiungiAiPreferiti_Forbidden() {

        UUID utenteId = UUID.randomUUID();
        Long eventoId = 1L;
        String internal = "false"; // Header non è "true"


        ResponseEntity<?> response = preferitiController.aggiungiAiPreferiti(internal, utenteId, eventoId);


        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testAggiungiAiPreferiti_MissingHeader() {

        UUID utenteId = UUID.randomUUID();
        Long eventoId = 1L;
        String internal = null; // Header mancante


        ResponseEntity<?> response = preferitiController.aggiungiAiPreferiti(internal, utenteId, eventoId);


        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testRimuoviDaiPreferiti_Success() {

        UUID utenteId = UUID.randomUUID();
        Long eventoId = 1L;
        String internal = "true";


        ResponseEntity<?> response = preferitiController.rimuoviDaiPreferiti(internal, utenteId, eventoId);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(preferitiService).findAndDelete(any(PreferitiDto.class));
    }

    @Test
    void testRimuoviDaiPreferiti_Forbidden() {

        UUID utenteId = UUID.randomUUID();
        Long eventoId = 1L;
        String internal = "false"; // Header non è "true"

        ResponseEntity<?> response = preferitiController.rimuoviDaiPreferiti(internal, utenteId, eventoId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testCreatePreferito_Success() {
        mockSecurityContext("utente1");

        when(eventoService.getByIdNoLocation(1L)).thenReturn(mockEventi.getFirst()); // Qualsiasi oggetto non-null
        when(preferitiService.getById(1L)).thenReturn(null); // Preferito non esiste ancora

        ResponseEntity<?> response = preferitiController.createPreferito(mockPreferiti.get(0));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(preferitiService).save(mockPreferiti.get(0));

        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreatePreferito_EventoNotFound() {

        PreferitiDto preferiti = createMockPreferitiDto(1L, "user123", 999L);

        when(eventoService.getByIdNoLocation(999L)).thenReturn(null); // Evento doesn't exist

        ResponseEntity<?> response = preferitiController.createPreferito(preferiti);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(preferitiService, never()).save(any());
    }

    @Test
    void testCreatePreferito_Unauthorized() {
        mockSecurityContext("utenteDiverso");

        when(eventoService.getByIdNoLocation(1L)).thenReturn(mockEventi.getFirst());
        when(preferitiService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = preferitiController.createPreferito(mockPreferiti.getFirst());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(preferitiService , never()).save(mockPreferiti.getFirst());

        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreatePreferito_Conflict() {

        mockSecurityContext("utente1");

        when(eventoService.getByIdNoLocation(1L)).thenReturn(mockEventi.getFirst());
        when(preferitiService.getById(1L)).thenReturn(mockPreferiti.getFirst()); // Preferito already exists

        ResponseEntity<?> response = preferitiController.createPreferito(mockPreferiti.getFirst());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        verify(preferitiService, never()).save(any());
    }

    @Test
    void testDeletePreferito_Success() {

        Long preferitoId = 1L;
        PreferitiDto esistente = createMockPreferitiDto(1L, "user123", 1L);
        mockSecurityContext("user123");

        when(preferitiService.getById(preferitoId)).thenReturn(esistente);

        preferitiController.deletePreferito(preferitoId);

        verify(preferitiService).delete(preferitoId);
    }

    @Test
    void testDeletePreferito_NotFound() {
        Long preferitoId = 999L;
        when(preferitiService.getById(preferitoId)).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> preferitiController.deletePreferito(preferitoId));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(preferitiService, never()).delete(any());
    }

    @Test
    void testDeletePreferito_Unauthorized() {
        Long preferitoId = 1L;
        PreferitiDto esistente = createMockPreferitiDto(1L, "user123", 1L);
        mockSecurityContext("differentUser"); // Different authenticated user

        when(preferitiService.getById(preferitoId)).thenReturn(esistente);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> preferitiController.deletePreferito(preferitoId));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        verify(preferitiService, never()).delete(any());
    }

    private EventoBasicDto createMockEvento(Long id, String nome) {
        EventoBasicDto evento = new EventoBasicDto();
        evento.setId(id);
        evento.setNome(nome);
        return evento;
    }

    private PreferitiDto createMockPreferitiDto(Long id, String utenteId, Long eventoId) {
        PreferitiDto preferiti = new PreferitiDto();
        preferiti.setId(id);
        preferiti.setUtente_id(utenteId);
        preferiti.setEvento_id(eventoId);
        return preferiti;
    }

    private void mockSecurityContext(String username) {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}