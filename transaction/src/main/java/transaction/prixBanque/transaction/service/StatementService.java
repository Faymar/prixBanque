package transaction.prixBanque.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transaction.prixBanque.transaction.model.Statement;
import transaction.prixBanque.transaction.model.Transaction;
import transaction.prixBanque.transaction.repository.StatementRepository;
import transaction.prixBanque.transaction.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatementService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private StatementRepository statementRepository;

    public Statement genererReleve(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        // Validation des dates
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La date de début doit être antérieure ou égale à la date de fin.");
        }

        // Récupérer les transactions pour la période donnée
        List<Transaction> transactions = transactionRepository.findByFromAccountIdAndDateBetween(accountId, startDate, endDate);

        // Si aucune transaction, créer un relevé vide
        if (transactions.isEmpty()) {
            Statement emptyStatement = new Statement();
            emptyStatement.setAccountId(accountId);
            emptyStatement.setStartDate(startDate);
            emptyStatement.setEndDate(endDate);
            emptyStatement.setTotalCredit(0.0);
            emptyStatement.setTotalDebit(0.0);
            emptyStatement.setBalance(0.0);
            return statementRepository.save(emptyStatement);
        }

        // Calculer les totaux de crédits et débits
        double totalCredit = transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("CREDIT"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalDebit = transactions.stream()
                .filter(t -> t.getType().equalsIgnoreCase("DEBIT"))
                .mapToDouble(Transaction::getAmount)
                .sum();

        // Calculer le solde
        double balance = totalCredit - totalDebit;
        if (balance < 0) {
            throw new IllegalArgumentException("Le solde calculé est négatif : " + balance);
        }
        // Créer un nouvel objet Statement
        Statement statement = new Statement();
        statement.setAccountId(accountId);
        statement.setStartDate(startDate);
        statement.setEndDate(endDate);
        statement.setTotalCredit(totalCredit);
        statement.setTotalDebit(totalDebit);
        statement.setBalance(balance);

        // Sauvegarder et retourner le relevé
        return statementRepository.save(statement);
    }


    public Statement obtenirReleveParId(Long statementId) {
        return statementRepository.findById(statementId).orElse(null);
    }

    public List<Statement> obtenirRelevesPourCompte(Long accountId) {
        return statementRepository.findAllByAccountId(accountId);
    }
}
