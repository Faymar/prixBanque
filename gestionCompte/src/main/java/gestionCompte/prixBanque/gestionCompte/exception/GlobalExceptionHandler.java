package gestionCompte.prixBanque.gestionCompte.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();

        if (ex.getCause() != null && ex.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
            java.sql.SQLIntegrityConstraintViolationException sqlException = (java.sql.SQLIntegrityConstraintViolationException) ex.getCause();

            response.put("error", "Integrity constraint violation");
            response.put("message", sqlException.getMessage());
            response.put("field", extractFieldFromMessage(sqlException.getMessage()));
            response.put("code", "DUPLICATE_ENTRY");
        } else {
            response.put("error", "Database constraint violation");
            response.put("message", ex.getMessage());
            response.put("code", "CONSTRAINT_VIOLATION");
        }

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    private String extractFieldFromMessage(String message) {
        if (message.contains("for key '")) {
            String[] parts = message.split("for key '");
            if (parts.length > 1) {
                return parts[1].replace("'", "");
            }
        }
        return "unknown_field";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication failed");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid username or password");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}

