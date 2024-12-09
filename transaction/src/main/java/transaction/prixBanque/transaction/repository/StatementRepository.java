package transaction.prixBanque.transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import transaction.prixBanque.transaction.model.Statement;

import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {

    List<Statement> findAllByAccountId(Long accountId);
}


