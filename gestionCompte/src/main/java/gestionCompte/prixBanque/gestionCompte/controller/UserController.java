package gestionCompte.prixBanque.gestionCompte.controller;

import gestionCompte.prixBanque.gestionCompte.model.Users;
import gestionCompte.prixBanque.gestionCompte.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Users user) {
        try {
            // Appeler la méthode `verify` qui renvoie le username, l'id et le token
            Map<String, Object> response = service.verify(user);
            return ResponseEntity.ok(response); // HTTP 200 avec le JSON
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Authentication failed",
                    "message", e.getMessage()
            ));
        }
    }
}
