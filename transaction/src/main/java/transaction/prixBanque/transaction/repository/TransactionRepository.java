package transaction.prixBanque.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transaction.prixBanque.transaction.model.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountId(Long fromAccountId);

    List<Transaction> findByFromAccountIdAndDateBetween(Long fromAccountId, LocalDateTime startDate, LocalDateTime endDate);
}
