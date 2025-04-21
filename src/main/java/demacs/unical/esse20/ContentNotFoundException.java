package demacs.unical.esse20;

public class ContentNotFoundException extends RuntimeException {
    /**
     * Eccezione per quando non viene trovato il dato richiesto
     * @see GlobalExceptionHandler
     */

    public ContentNotFoundException(String message) {
        super(message);
    }
}
