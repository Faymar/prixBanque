package gestionCompte.prixBanque.gestionCompte.service;

import gestionCompte.prixBanque.gestionCompte.model.*;
import org.springframework.security.core.AuthenticationException;
import gestionCompte.prixBanque.gestionCompte.repository.AccountRepo;
import gestionCompte.prixBanque.gestionCompte.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AccountRepo accountRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        // Encode the user's password
        user.setPassword(encoder.encode(user.getPassword()));

        // Save the user
        Users savedUser = userRepo.save(user);

        // Create a default Credit account for the user
        Account creditAccount = new Account();
        creditAccount.setBalance(0.0); // Initial balance
        creditAccount.setStatus(Status.ACTIVE); // Assuming Status is an enum
        creditAccount.setAccountType(AccountType.DEBIT); // Assuming AccountType is an enum

        // Save the account
        Account savedAccount = accountRepo.save(creditAccount);

        // Associate the account with the user
        savedUser.getAccounts().add(savedAccount);
        userRepo.save(savedUser);

        return savedUser;
    }


    public Map<String, Object> verify(Users user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                // Récupérez le principal et assurez-vous qu'il est de type UserPrincipal
                Object principal = authentication.getPrincipal();

                if (principal instanceof UserPrincipal) {
                    UserPrincipal userPrincipal = (UserPrincipal) principal;

                    // Récupérez l'objet Users depuis UserPrincipal
                    Users authenticatedUser = userPrincipal.getUser();

                    // Générez le token avec username et id
                    String token = jwtService.generateToken(authenticatedUser.getUsername(), (long) authenticatedUser.getId());

                    // Préparez la réponse
                    Map<String, Object> response = new HashMap<>();
                    response.put("username", authenticatedUser.getUsername());
                    response.put("id", authenticatedUser.getId());
                    response.put("token", token);

                    return response;
                } else {
                    throw new IllegalArgumentException("Invalid user principal type");
                }
            } else {
                throw new IllegalArgumentException("Authentication failed");
            }
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }


}
