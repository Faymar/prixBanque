package gestionCompte.prixBanque.gestionCompte.repository;

import gestionCompte.prixBanque.gestionCompte.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {


    boolean existsByIdAndUsers_Id(Long accountId, Long userId);
}
