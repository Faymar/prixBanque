package gestionCompte.prixBanque.gestionCompte.service;

import gestionCompte.prixBanque.gestionCompte.model.Account;
import gestionCompte.prixBanque.gestionCompte.model.Users;
import gestionCompte.prixBanque.gestionCompte.repository.AccountRepo;
import gestionCompte.prixBanque.gestionCompte.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;

    public AccountService(AccountRepo accountRepo, UserRepo userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }

    public Account createAccountForUser(Long userId, Account account) {
        Users user = userRepo.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        account.getUsers().add(user);
        Account savedAccount = accountRepo.save(account);

        user.getAccounts().add(savedAccount);
        userRepo.save(user);

        return savedAccount;
    }

    public boolean verifierCompte(Long clientId, Long accountId) {
        // Vérifier si une relation existe entre le compte et l'utilisateur
        return accountRepo.existsByIdAndUsers_Id(accountId, clientId);
    }

}