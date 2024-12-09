package transaction.prixBanque.transaction.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transaction.prixBanque.transaction.model.Transaction;
import transaction.prixBanque.transaction.service.JWTService;
import transaction.prixBanque.transaction.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JWTService jwtService;

    private boolean verifierProprietaireCompte(HttpServletRequest request, Long accountId) {
        String token = request.getHeader("Authorization").substring(7);
        String clientIdString = jwtService.extractUserId(token); // Extraction du clientId sous forme de chaîne
        Long clientId = Long.valueOf(clientIdString); // Conversion en Long

        return clientId.equals(accountId);
    }

    // Effectuer une transaction
    @PostMapping("/effectuer/{accountId}")
    public ResponseEntity<?> effectuerTransaction(
            HttpServletRequest request,
            @PathVariable Long accountId,
            @Valid @RequestBody Transaction transaction) {

        // Vérification de la propriété du compte
        if (!verifierProprietaireCompte(request,accountId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Le compte avec ID " + accountId + " n'appartient pas au client !");
        }

        // Effectuer la transaction
        Transaction savedTransaction = transactionService.effectuerTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }

    // Obtenir les transactions pour un compte entre deux dates
    @GetMapping("/{accountId}")
    public ResponseEntity<?> obtenirTransactions(
            HttpServletRequest request,
            @PathVariable Long accountId) {

        // Vérification de la propriété du compte
        if (!verifierProprietaireCompte(request, accountId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Le compte avec ID " + accountId + " n'appartient pas au client !");
        }

        // Récupération des transactions
        List<Transaction> transactions = transactionService.obtenirTransactions(accountId);

        if (transactions == null || transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Aucune transaction trouvée pour le compte ID " + accountId + " entre les dates spécifiées !");
        }

        return ResponseEntity.ok(transactions);
    }
}
