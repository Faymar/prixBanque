package gestionCompte.prixBanque.gestionCompte.controller;

import gestionCompte.prixBanque.gestionCompte.model.Account;
import gestionCompte.prixBanque.gestionCompte.model.Users;
import gestionCompte.prixBanque.gestionCompte.repository.UserRepo;
import gestionCompte.prixBanque.gestionCompte.service.AccountService;
import gestionCompte.prixBanque.gestionCompte.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody Account account, HttpServletRequest request) {
        try {
            String token = request.getHeader("Authorization").substring(7);
            String clientIdString = jwtService.extractUserId(token); // Extraction du clientId sous forme de chaîne
            Long clientId = Long.valueOf(clientIdString);

            Users user = userRepo.findById(clientId);
            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            Account createdAccount = accountService.createAccountForUser((long) user.getId(), account);
            return ResponseEntity.ok(createdAccount);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{clientId}/validate/{accountId}")
    public ResponseEntity<String> verifierCompte(@PathVariable Long clientId, @PathVariable Long accountId) {
        boolean compteValide = accountService.verifierCompte(clientId, accountId);

        if (!compteValide) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Le compte avec ID " + accountId + " n'appartient pas au client avec ID " + clientId);
        }

        return ResponseEntity.ok("Le compte avec ID " + accountId + " appartient bien au client avec ID " + clientId);
    }

//    @GetMapping("/{clientId}/validate/{accountId}")
//    public ResponseEntity<Boolean> verifierCompte(@PathVariable Long clientId, @PathVariable Long accountId) {
//        boolean compteValide = accountService.verifierCompte(clientId, accountId);
//
//        if (!compteValide) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
//        }
//        return ResponseEntity.ok(true);
//    }

}
