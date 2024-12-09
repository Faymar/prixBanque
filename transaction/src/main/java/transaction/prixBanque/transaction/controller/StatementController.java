package transaction.prixBanque.transaction.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import transaction.prixBanque.transaction.exception.ResourceNotFoundException;
import transaction.prixBanque.transaction.model.Statement;
import transaction.prixBanque.transaction.service.JWTService;
import transaction.prixBanque.transaction.service.StatementService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/releves")
public class StatementController {

    @Autowired
    private StatementService statementService;

    @Autowired
    private JWTService jwtService;


    private boolean verifierProprietaireCompte(HttpServletRequest request, Long accountId) {
        String token = request.getHeader("Authorization").substring(7);
        String clientIdString = jwtService.extractUserId(token); // Extraction du clientId sous forme de chaîne
        Long clientId = Long.valueOf(clientIdString); // Conversion en Long

        return clientId.equals(accountId);
    }

    @PostMapping("/generate/{accountId}")
    public ResponseEntity<?> genererReleve(
            HttpServletRequest request,
            @PathVariable Long accountId,
            @RequestBody @Valid ReleveRequest releveRequest) {

        // Vérification de la propriété du compte
        if (!verifierProprietaireCompte(request, accountId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Le compte avec ID " + accountId + " n'appartient pas au client !");
        }

        // Générer le relevé
        Statement statement = statementService.genererReleve(accountId, releveRequest.getStartDate(), releveRequest.getEndDate());

        if (statement == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Impossible de générer le relevé pour le compte ID " + accountId + ".");
        }

        return ResponseEntity.ok(statement);
    }



    @GetMapping("/{statementId}/{accountId}")
    public ResponseEntity<?> obtenirReleveParId(
            HttpServletRequest request,
            @PathVariable Long statementId,
            @PathVariable Long accountId) {

        // Vérification de la propriété du compte
        if (!verifierProprietaireCompte(request, accountId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Le compte avec ID " + accountId + " n'appartient pas au client !");
        }

        // Récupération du relevé par ID
        Statement statement = statementService.obtenirReleveParId(statementId);
        if (statement == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Le relevé avec ID " + statementId + " est introuvable !");
        }
        return ResponseEntity.ok(statement);
    }


    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> obtenirRelevesPourCompte(
            HttpServletRequest request,
            @PathVariable Long accountId) {
        // Si le compte est valide, récupérer les relevés
        if (verifierProprietaireCompte(request, accountId)) {
            List<Statement> statements = statementService.obtenirRelevesPourCompte(accountId);
            return ResponseEntity.ok(statements);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Le compte avec ID " + accountId + " n'appartient pas au client !");
    }

}
