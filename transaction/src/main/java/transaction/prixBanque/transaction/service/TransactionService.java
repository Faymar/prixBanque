package transaction.prixBanque.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import transaction.prixBanque.transaction.model.Transaction;
import transaction.prixBanque.transaction.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction effectuerTransaction(Transaction transaction) {
        transaction.setDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> obtenirTransactions(Long accountId) {
        return transactionRepository.findByFromAccountId(accountId);
    }

    public List<Transaction> obtenirTransactionsEntreDates(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByFromAccountIdAndDateBetween(accountId, startDate, endDate);
    }
}



