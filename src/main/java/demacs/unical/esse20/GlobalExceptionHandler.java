package demacs.unical.esse20;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.nio.file.AccessDeniedException;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={AccessDeniedException.class})
    public ResponseEntity<String> handleDeniedAccessException(AccessDeniedException e) {
        return new ResponseEntity<>(new JSONObject(Map.of("error", e.getMessage())).toString(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<String> handleException(ContentNotFoundException e) {
        return new ResponseEntity<>( new JSONObject(Map.of("error", e.getMessage())).toString(), HttpStatus.NOT_FOUND);
    }

}
