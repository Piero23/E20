package org.unical.enterprise.eventoLocation;

public class ContentNotFoundException extends RuntimeException {
    /**
     * Eccezione per quando non viene trovato il dato richiesto
     * @see GlobalExceptionHandler
     */

    public ContentNotFoundException(String message) {
        super(message);
    }
}
